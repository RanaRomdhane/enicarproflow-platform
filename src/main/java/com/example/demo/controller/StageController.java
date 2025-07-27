package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.example.demo.dto.CreateStageRequest;
import com.example.demo.dto.StageDTO;
import com.example.demo.entity.Etudiant;
import com.example.demo.entity.Enseignant;
import com.example.demo.entity.Stage;
import com.example.demo.entity.StageStatus;
import com.example.demo.repository.EtudiantRepository;
import com.example.demo.repository.EnseignantRepository;
import com.example.demo.services.StageService;

@RestController
@RequestMapping("/api/stages")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class StageController {
    
    private final StageService stageService;
    private final EtudiantRepository etudiantRepo;
    private final EnseignantRepository enseignantRepo;

    @GetMapping
    public ResponseEntity<List<StageDTO>> getAllStages() {
        return ResponseEntity.ok(stageService.getAllStages());
    }

    @PostMapping
    public ResponseEntity<StageDTO> createStage(@RequestBody CreateStageRequest req) {
        // 1) Load the Etudiant and Enseignant from their IDs
        Etudiant etu = etudiantRepo.findById(req.getEtudiantId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Etudiant with id " + req.getEtudiantId() + " not found"));

        // 2) Map to your entity
        Stage s = new Stage();
        s.setEntreprise(req.getEntreprise());
        s.setProjet(req.getProjet());
        s.setDateDebut(req.getDateDebut());
        s.setDateFin(req.getDateFin());
        s.setLieu(req.getLieu());
        s.setEncadrantIndustriel(req.getEncadrantIndustriel());
        s.setEmailEncadrant(req.getEmailEncadrant());
        s.setDescription(req.getDescription());
        s.setStatus(req.getStatus());
        s.setEtudiant(etu);

        // 3) Delegate to your service
        StageDTO created = stageService.createStage(s);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StageDTO> getStageById(@PathVariable Integer id) {
        StageDTO stage = stageService.getStageById(id);
        return (stage != null)
            ? ResponseEntity.ok(stage)
            : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/validate")
    public ResponseEntity<StageDTO> validateStage(@PathVariable Integer id) {
        return ResponseEntity.ok(stageService.validateStage(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<StageDTO> updateStageStatus(
            @PathVariable Integer id,
            @RequestParam StageStatus status) {
        return ResponseEntity.ok(stageService.updateStageStatus(id, status));
    }
    
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<StageDTO>> getStagesByEtudiant(@PathVariable Integer etudiantId) {
        return ResponseEntity.ok(stageService.getStagesByEtudiant(etudiantId));
    }

    @GetMapping("/nbStage")
    public int nombreDeStage() {
    	return stageService.nbreStage();
    }
}
