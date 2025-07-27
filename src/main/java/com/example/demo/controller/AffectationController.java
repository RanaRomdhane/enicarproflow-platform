package com.example.demo.controller;


import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AffectationDTO;
import com.example.demo.services.AffectationService;

@RestController
@RequestMapping("/api/affectations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AffectationController {
    private final AffectationService affectationService;

    @PostMapping("/automatique")
    public ResponseEntity<String> lancerAffectationAutomatique() {
        try {
            affectationService.executerAffectationAutomatique();
            return ResponseEntity.ok("L'affectation automatique a été exécutée avec succès");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erreur lors de l'affectation automatique: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<List<AffectationDTO>> listerToutesAffectations() {
        return ResponseEntity.ok(affectationService.listerToutesAffectations());
    }

   /* @GetMapping("/etudiant/{id}")
    public ResponseEntity<AffectationDTO> getAffectationParEtudiant(@PathVariable Long id) {
        return affectationService.trouverAffectationParEtudiant(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }*/
    
    @GetMapping("/etudiant/{id}")
    public ResponseEntity<?> getAffectationByEtudiant(@PathVariable Long id) {
        return affectationService.trouverAffectationParEtudiant(id)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.ok(Map.of("message", "Aucune affectation trouvée pour cet étudiant")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerAffectation(@PathVariable Long id) {
        affectationService.supprimerAffectation(id);
        return ResponseEntity.noContent().build();
    }
    
}
