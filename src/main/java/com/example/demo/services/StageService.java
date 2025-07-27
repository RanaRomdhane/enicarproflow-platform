package com.example.demo.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.demo.dto.StageDTO;
import com.example.demo.entity.Document;
import com.example.demo.entity.Stage;
import com.example.demo.entity.StageStatus;
import com.example.demo.entity.TypeDocument;
import com.example.demo.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StageService {
    private final StageRepository stageRepository;
    private final EtudiantService etudiantService;
    private final EnseignantService enseignantService;
    private final DocumentService documentService;

    public StageDTO createStage(Stage stage) {
        stage.setStatus(StageStatus.EN_ATTENTE);
        return convertToDTO(stageRepository.save(stage));
    }

    public List<StageDTO> getAllStages() {
        return stageRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StageDTO validateStage(Integer id) {
        Stage stage = stageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stage non trouvé"));
        stage.setStatus(StageStatus.VALIDE);
        return convertToDTO(stageRepository.save(stage));
    }

    public StageDTO updateStageStatus(Integer id, StageStatus status) {
        Stage stage = stageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stage non trouvé"));
        stage.setStatus(status);
        return convertToDTO(stageRepository.save(stage));
    }

    // Method to get stage by ID
    public StageDTO getStageById(Integer id) {
        Stage stage = stageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stage non trouvé"));
        return convertToDTO(stage);
    }
    private StageDTO convertToDTO(Stage stage) {
        StageDTO dto = new StageDTO();
        dto.setId(stage.getId());
        dto.setEtudiant(stage.getEtudiant().getPrenom() + " " + stage.getEtudiant().getNom());
        dto.setEntreprise(stage.getEntreprise());
        dto.setProjet(stage.getProjet());
        dto.setDateDebut(stage.getDateDebut());
        dto.setDateFin(stage.getDateFin());
        dto.setStatus(stage.getStatus().toString());

        List<Document> docs = Optional
            .ofNullable(stage.getDocuments())
            .orElseGet(Collections::emptyList);

        boolean hasReport = docs.stream()
            .anyMatch(d -> d.getType() == TypeDocument.RAPPORT && "Validé".equalsIgnoreCase(d.getStatus()));
        dto.setReportStatus(hasReport ? "Soumis" : "Non soumis");

        return dto;
    }



    public List<StageDTO> getStagesByEtudiant(Integer etudiantId) {
        return stageRepository.findByEtudiant_IdEtudiant(etudiantId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public int nbreStage() {
    	return stageRepository.nombreStage();
    }
    }
