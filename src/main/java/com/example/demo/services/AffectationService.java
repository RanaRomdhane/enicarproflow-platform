package com.example.demo.services;

import java.util.Comparator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AffectationDTO;
import com.example.demo.entity.Affectation;
import com.example.demo.entity.ChoixEtudiant;
import com.example.demo.entity.Enseignant;
import com.example.demo.entity.Etudiant;
import com.example.demo.entity.Sujet;
import com.example.demo.repository.AffectationRepository;
import com.example.demo.repository.ChoixEtudiantRepository;
import com.example.demo.repository.SujetRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class AffectationService {
    private final ChoixEtudiantRepository choixRepo;
    private final AffectationRepository affectationRepo;
    private final SujetRepository sujetRepo;
    private final NotificationService notificationService;

    public void executerAffectationAutomatique() {
        // 1. Traiter d'abord les sujets proposés par les étudiants
        List<ChoixEtudiant> propositionsEtudiants = choixRepo.findByEstProposeTrue();
        traiterPropositionsEtudiants(propositionsEtudiants);

        // 2. Affecter les sujets standard selon le mérite
        //List<Sujet> sujetsStandard = sujetRepo.findByEstValideTrueAndEstProposeFalse();
       // sujetsStandard.forEach(this::affecterSujetSelonMerite);
        sujetRepo.findSujetsValidesNonAffectes().forEach(this::affecterSujetSelonMerite);
    }

    private void traiterPropositionsEtudiants(List<ChoixEtudiant> propositions) {
        propositions.forEach(choix -> {
            if (choix.getSujet().getEstValide()) {
                creerAffectation(
                    choix.getSujet(),
                    choix.getEtudiant(),
                    choix.getBinome(),
                    choix.getSujet().getEncadrant()
                );
            }
        });
    }

    private void affecterSujetSelonMerite(Sujet sujet) {
        List<ChoixEtudiant> choix = choixRepo.findBySujetOrderByOrdrePreference(sujet);
        
        choix.stream()
            .filter(c -> !etudiantDejaAffecte(c.getEtudiant()))
            .sorted(Comparator.comparing(
                c -> c.getEtudiant().getMoyenne(), 
                Comparator.reverseOrder()
            ))
            .findFirst()
            .ifPresent(meilleurChoix -> {
                creerAffectation(
                    sujet,
                    meilleurChoix.getEtudiant(),
                    meilleurChoix.getBinome(),
                    sujet.getEncadrant()
                );
            });
    }

    private void creerAffectation(Sujet sujet, Etudiant etudiant1, Etudiant etudiant2, Enseignant encadrant) {
        Affectation affectation = new Affectation();
        affectation.setSujet(sujet);
        affectation.setEtudiant1(etudiant1);
        affectation.setEtudiant2(etudiant2);
        affectation.setEncadrant(encadrant);
        affectation.setDateAffectation(LocalDate.now());
        
        //affectationRepo.save(affectation);
        
         // 2. Lier le sujet à l'affectation (relation bidirectionnelle)
        sujet.setAffectation(affectation);

        // 3. Sauvegarder
        affectationRepo.save(affectation);
        sujetRepo.save(sujet);
        
        // Notifications
        notificationService.notifierAffectation(affectation);
    }

    private boolean etudiantDejaAffecte(Etudiant etudiant) {
        return affectationRepo.existsByEtudiant1OrEtudiant2(etudiant, etudiant);
    }

    public List<AffectationDTO> listerToutesAffectations() {
        return affectationRepo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AffectationDTO> trouverAffectationParEtudiant(Long etudiantId) {
        return affectationRepo.findByEtudiant1_IdEtudiantOrEtudiant2_IdEtudiant(etudiantId, etudiantId)
                .map(this::convertToDTO);
    }

    public void supprimerAffectation(Long affectationId) {
        // 1. Trouver l'affectation
        Affectation affectation = affectationRepo.findById(affectationId)
                .orElseThrow(() -> new EntityNotFoundException("Affectation non trouvée"));
        
        // 2. Supprimer la référence dans le sujet
        Sujet sujet = affectation.getSujet();
        sujet.setAffectation(null);
        sujetRepo.save(sujet);
        
        // 3. Supprimer l'affectation
        affectationRepo.delete(affectation);
        
        // 4. Notifier si nécessaire
        notificationService.notifierSuppressionAffectation(affectation);
    }
    
 // Méthode utilitaire de conversion
    private AffectationDTO convertToDTO(Affectation a) {
        String enseignantName;
        if (a.getEncadrant() != null) {
            enseignantName = a.getEncadrant().getPrenom() + " " + a.getEncadrant().getNom();
        } else {
            enseignantName = "Non assigné";
        }

        return AffectationDTO.builder()
                .id(a.getId())
                .etudiant1(a.getEtudiant1().getPrenom() + " " + a.getEtudiant1().getNom())
                .etudiant2(a.getEtudiant2() != null
                          ? a.getEtudiant2().getPrenom() + " " + a.getEtudiant2().getNom()
                          : null)
                .sujet(a.getSujet().getTitre())
                .enseignant(enseignantName)
                .dateAffectation(a.getDateAffectation())
                .build();
    }

   
}