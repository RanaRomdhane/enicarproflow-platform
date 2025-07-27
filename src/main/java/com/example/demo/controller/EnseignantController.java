package com.example.demo.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.EnseignantDTO;
import com.example.demo.dto.EtudiantDTO;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.ProjetEncadreDTO;
import com.example.demo.dto.ProjetProposeDTO;
import com.example.demo.services.EnseignantService;

import java.util.List;

@RestController
@RequestMapping("/api/enseignants")
@CrossOrigin(origins = "http://localhost:3000")
public class EnseignantController {

    private final EnseignantService enseignantService;

    public EnseignantController(EnseignantService enseignantService) {
        this.enseignantService = enseignantService;
    }


    // Récupérer le profil enseignant
    @GetMapping("/{id}/profil")
    public ResponseEntity<?> getProfilEnseignant(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(enseignantService.getProfilEnseignant(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer les projets encadrés
    @GetMapping("/{id}/projets-encadres")
    public ResponseEntity<List<ProjetEncadreDTO>> getProjetsEncadres(@PathVariable Integer id) {
        return ResponseEntity.ok(enseignantService.getProjetsEncadres(id));
    }

    // Valider un document
    @PostMapping("/{enseignantId}/documents/{docId}/valider")
    public ResponseEntity<?> validerDocument(
            @PathVariable Integer enseignantId,
            @PathVariable Integer docId) {
        try {
            enseignantService.validerDocument(docId, enseignantId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Proposer un nouveau sujet
    @PostMapping("/{id}/proposer-sujet")
    public ResponseEntity<?> proposerSujet(
            @PathVariable Integer id,
            @RequestBody ProjetProposeDTO projetDTO) {
        try {
            return ResponseEntity.ok(enseignantService.proposerSujet(id, projetDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Lister les sujets proposés
    @GetMapping("/{id}/sujets-proposes")
    public ResponseEntity<List<ProjetProposeDTO>> getSujetsProposes(@PathVariable Integer id) {
        // Ensure the service returns a list of ProjetProposeDTO objects
        List<ProjetProposeDTO> sujetsProposes = enseignantService.getSujetsProposes(id);
        return ResponseEntity.ok(sujetsProposes);
    }

}