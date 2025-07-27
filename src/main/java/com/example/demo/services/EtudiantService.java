package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.EtudiantDTO;
import com.example.demo.entity.Etudiant;
import com.example.demo.repository.EtudiantRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    @Autowired
    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    public List<EtudiantDTO> getAllEtudiants() {
        try {
            List<Etudiant> etudiants = etudiantRepository.findAll();
            return etudiants.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            throw e;
        }
    }

    public EtudiantDTO getEtudiantById(Integer id) {
        Optional<Etudiant> etudiant = etudiantRepository.findById(id);
        if (etudiant.isEmpty()) {
            throw new RuntimeException("Étudiant non trouvé avec l'id: " + id);
        }
        return convertToDTO(etudiant.get());
    }

    public EtudiantDTO createEtudiant(EtudiantDTO etudiantDTO) {
        Etudiant etudiant = convertToEntity(etudiantDTO);
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);
        return convertToDTO(savedEtudiant);
    }

    public EtudiantDTO updateEtudiant(Integer id, EtudiantDTO etudiantDTO) {
        Optional<Etudiant> existingEtudiant = etudiantRepository.findById(id);
        if (existingEtudiant.isEmpty()) {
            throw new RuntimeException("Étudiant non trouvé avec l'id: " + id);
        }

        Etudiant etudiant = existingEtudiant.get();
        updateEntityFromDTO(etudiant, etudiantDTO);
        
        Etudiant updatedEtudiant = etudiantRepository.save(etudiant);
        return convertToDTO(updatedEtudiant);
    }
    
    private void updateEntityFromDTO(Etudiant etudiant, EtudiantDTO dto) {
        if (dto.getPrenom() != null) etudiant.setPrenom(dto.getPrenom());
        if (dto.getNom() != null) etudiant.setNom(dto.getNom());
        if (dto.getEmail() != null) etudiant.setEmail(dto.getEmail());
        if (dto.getSection() != null) etudiant.setSection(dto.getSection());
        if (dto.getGroupe() != null) etudiant.setGroupe(dto.getGroupe());
        if (dto.getMoyenne() != null) etudiant.setMoyenne(dto.getMoyenne());
        if (dto.getOrdreMerite() != null) etudiant.setOrdreMerite(dto.getOrdreMerite());

        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isEmpty()) {
            etudiant.setMotDePasse(dto.getMotDePasse()); // Stockage en clair (à éviter en production)
        }
    }

    public void deleteEtudiant(Integer id) {
        if (!etudiantRepository.existsById(id)) {
            throw new RuntimeException("Étudiant non trouvé avec l'id: " + id);
        }
        etudiantRepository.deleteById(id);
    }

    public List<EtudiantDTO> getEtudiantsBySection(String section) {
        return etudiantRepository.findBySection(section).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EtudiantDTO> getEtudiantsByGroupe(String groupe) {
        return etudiantRepository.findByGroupe(groupe).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EtudiantDTO> getEtudiantsByEncadrant(Integer encadrantId) {
        return etudiantRepository.findByEncadrant_IdEnseignant(encadrantId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EtudiantDTO> getClassementEtudiants() {
        return etudiantRepository.findByOrderByMoyenneDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public EtudiantDTO authenticate(String email, String motDePasse) {
        if (email == null || motDePasse == null) {
            return null;
        }
        Optional<Etudiant> etudiantOpt = etudiantRepository.findByEmailAndMotDePasse(email, motDePasse);
        return etudiantOpt.map(this::convertToDTO).orElse(null);
    }

    private EtudiantDTO convertToDTO(Etudiant etudiant) {
        if (etudiant == null) {
            return null;
        }
        
        EtudiantDTO dto = new EtudiantDTO();
        dto.setId(etudiant.getIdEtudiant());
        dto.setPrenom(etudiant.getPrenom());
        dto.setNom(etudiant.getNom());
        dto.setEmail(etudiant.getEmail());
        dto.setSection(etudiant.getSection());
        dto.setGroupe(etudiant.getGroupe());
        dto.setMoyenne(etudiant.getMoyenne());
        dto.setOrdreMerite(etudiant.getOrdreMerite());
        // Ne pas remplir le mot de passe dans la réponse pour des raisons de sécurité
        // dto.setMotDePasse(etudiant.getMotDePasse());
        
        if (etudiant.getEncadrant() != null) {
            dto.setEncadrantId(etudiant.getEncadrant().getIdEnseignant());
            dto.setEncadrantNom(etudiant.getEncadrant().getNomComplet());
        }
        return dto;
    }

    private Etudiant convertToEntity(EtudiantDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Etudiant etudiant = new Etudiant();
        etudiant.setPrenom(dto.getPrenom());
        etudiant.setNom(dto.getNom());
        etudiant.setEmail(dto.getEmail());
        etudiant.setSection(dto.getSection());
        etudiant.setGroupe(dto.getGroupe());
        etudiant.setMoyenne(dto.getMoyenne());
        etudiant.setOrdreMerite(dto.getOrdreMerite());
        etudiant.setMotDePasse(dto.getMotDePasse()); // À sécuriser en production
        // L'encadrant sera géré séparément
        return etudiant;
    }
    
    
    public int nbreEtudiants() {
    	return etudiantRepository.nbEtd();
    }
}