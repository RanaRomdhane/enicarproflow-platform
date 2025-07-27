package com.example.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import com.example.demo.dto.DocumentDTO;
import com.example.demo.dto.EtudiantDTO;
import com.example.demo.entity.Document;
import com.example.demo.entity.Etudiant;
import com.example.demo.entity.Stage;
import com.example.demo.entity.StageStatus;
import com.example.demo.entity.TypeDocument;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.StageRepository;

import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final StageRepository stageRepository;
    private final EtudiantService etudiantService;

    private final Path rootLocation = Paths.get("uploads/documents");
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    public DocumentDTO uploadDocument(
            Integer stageId,
            MultipartFile file,
            TypeDocument type,
            Integer etudiantId) throws IOException {

        // 1) Prépare le dossier
        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }

        // 2) Sauvegarde physique
        String originalFilename = file.getOriginalFilename();
        String extension        = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String newFilename      = UUID.randomUUID() + extension;
        Path dest               = rootLocation.resolve(newFilename);
        Files.copy(file.getInputStream(), dest);

        // 3) Construction de l'entité Document
        Document doc = new Document();
        doc.setNom(originalFilename);
        doc.setCheminFichier(dest.toString());
        doc.setTypeFichier(file.getContentType());
        doc.setType(type);
        doc.setDateUpload(LocalDate.now());
        doc.setStatus("En attente");

        // 4) Association Etudiant
        EtudiantDTO etuDto = etudiantService.getEtudiantById(etudiantId);
        Etudiant etu = new Etudiant();
        etu.setIdEtudiant(etuDto.getId());
        etu.setPrenom(etuDto.getPrenom());
        etu.setNom(etuDto.getNom());
        doc.setEtudiant(etu);

        // 5) Association facultative du Stage
        if (stageId != null) {
            stageRepository.findById(stageId)
                .ifPresentOrElse(
                    doc::setStage,
                    () -> logger.warn("Stage {} non trouvé, on l’ignore", stageId)
                );
        }

        // 6) Persistance et MAJ du statut de stage
        Document saved = documentRepository.save(doc);
        if (saved.getStage() != null) {
            updateStageStatusBasedOnDocuments(saved.getStage());
        }

        // 7) Conversion en DTO
        return convertToDTO(saved);
    }

    private DocumentDTO convertToDTO(Document document) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(document.getIdDocument());
        dto.setNom(document.getNom());
        dto.setType(document.getType().toString());
        dto.setStatus(document.getStatus());
        dto.setDateUpload(document.getDateUpload());
        dto.setEtudiantId(document.getEtudiant().getIdEtudiant());
        dto.setStageId(document.getStage() != null
            ? document.getStage().getId()
            : null);
        dto.setNomFichier(document.getCheminFichier());
        return dto;
    }

    public List<DocumentDTO> getDocumentsWithStage() {
        return documentRepository.findByStageIsNotNull().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DocumentDTO> getDocumentsWithoutStage() {
        return documentRepository.findByStageIsNull().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public DocumentDTO validateDocument(Integer documentId) {
        Document doc = documentRepository.findById(documentId)
            .orElseThrow(() -> new EntityNotFoundException("Document non trouvé"));
        doc.setStatus("Validé");
        Document updated = documentRepository.save(doc);

        // On ne met à jour le stage que si on en a un
        if (updated.getStage() != null) {
            updateStageStatusBasedOnDocuments(updated.getStage());
        }

        return convertToDTO(updated);
    }

    public DocumentDTO rejectDocument(Integer documentId, String reason) {
        Document doc = documentRepository.findById(documentId)
            .orElseThrow(() -> new EntityNotFoundException("Document non trouvé"));
        doc.setStatus("Rejeté");
        Document updated = documentRepository.save(doc);
        return convertToDTO(updated);
    }

    public List<DocumentDTO> getDocumentsByStage(Integer stageId) {
        return documentRepository.findByStageId(stageId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DocumentDTO> getDocumentsByEtudiant(Integer etudiantId) {
        return documentRepository.findByEtudiant_IdEtudiant(etudiantId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<DocumentDTO> getAllDocuments() {
        return documentRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private void updateStageStatusBasedOnDocuments(Stage stage) {
        if (stage == null) {
            return;
        }

        List<Document> docs = documentRepository.findByStageId(stage.getId());

        boolean hasValidRelevantDoc = docs.stream()
            .anyMatch(d ->
                "Validé".equals(d.getStatus()) &&
                (
                    d.getType() == TypeDocument.RAPPORT ||
                    d.getType() == TypeDocument.ATTESTATION ||
                    d.getType() == TypeDocument.POSTER
                )
            );

        if (hasValidRelevantDoc && stage.getStatus() == StageStatus.EN_COURS) {
            stage.setStatus(StageStatus.VALIDE);
            stageRepository.save(stage);
        }
    }

    public Resource downloadDocument(Integer documentId) {
        Document document = documentRepository.findById(documentId)
            .orElseThrow(() -> new EntityNotFoundException("Document non trouvé"));
        try {
            Path filePath = Paths.get(document.getCheminFichier())
                                 .toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new RuntimeException("Impossible de lire le fichier : " + document.getCheminFichier());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erreur lors du chargement du fichier : " + e.getMessage(), e);
        }
    }

    public DocumentDTO getDocumentById(Integer documentId) {
        Document document = documentRepository.findById(documentId)
            .orElseThrow(() -> new EntityNotFoundException("Document non trouvé"));
        return convertToDTO(document);
    }
}
