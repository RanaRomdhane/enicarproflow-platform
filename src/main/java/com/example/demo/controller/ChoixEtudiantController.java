package com.example.demo.controller;

import com.example.demo.entity.ChoixEtudiant;
import com.example.demo.entity.Etudiant;
import com.example.demo.repository.EtudiantRepository;
import com.example.demo.services.ChoixEtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/choix")
@CrossOrigin(origins = "http://localhost:3000")
public class ChoixEtudiantController {

    private final ChoixEtudiantService choixEtudiantService;
    private final EtudiantRepository etudiantRepository;  // Add the EtudiantRepository

    @Autowired
    public ChoixEtudiantController(ChoixEtudiantService choixEtudiantService, EtudiantRepository etudiantRepository) {
        this.choixEtudiantService = choixEtudiantService;
        this.etudiantRepository = etudiantRepository;
    }

    // Créer un nouveau choix d'étudiant (POST)
    @PostMapping
    public ResponseEntity<ChoixEtudiant> createChoixEtudiant(@RequestBody ChoixEtudiant choixEtudiant) {
        // Si le binôme est nul ou non renseigné, on le laisse tel quel
        if (choixEtudiant.getBinome() == null) {
            choixEtudiant.setBinome(null);
        }

        // Fetch the existing Etudiant by ID
        Etudiant etudiant = etudiantRepository.findById(choixEtudiant.getEtudiant().getIdEtudiant())
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));

        // Set the existing Etudiant to the ChoixEtudiant
        choixEtudiant.setEtudiant(etudiant);

        // Création du choix d'étudiant
        ChoixEtudiant createdChoix = choixEtudiantService.createChoixEtudiant(choixEtudiant);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChoix);
    }

    // Récupérer les choix d'un étudiant spécifique (GET)
    @GetMapping("/{idEtudiant}")
    public ResponseEntity<?> getChoixByEtudiant(@PathVariable Integer idEtudiant) {
        return ResponseEntity.ok(choixEtudiantService.findByEtudiantId(idEtudiant));
    }
    
    @GetMapping("/nbEtudiant")
    public int choixEtudiant() {
    	return choixEtudiantService.nbreEtudiant();
    }
}
