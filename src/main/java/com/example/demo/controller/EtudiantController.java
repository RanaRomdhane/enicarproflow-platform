package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.EtudiantDTO;
import com.example.demo.dto.LoginRequest;
import com.example.demo.services.EtudiantService;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/etudiants")
@CrossOrigin(origins = "http://localhost:3000")
public class EtudiantController {

    private static final Logger logger = Logger.getLogger(EtudiantController.class.getName());
    private final EtudiantService etudiantService;

    @Autowired
    public EtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @GetMapping
    public ResponseEntity<?> getAllEtudiants() {
        try {
            List<EtudiantDTO> etudiants = etudiantService.getAllEtudiants();
            return ResponseEntity.ok(etudiants);
        } catch (Exception e) {
            logger.severe("Error getting all etudiants: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching students: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEtudiantById(@PathVariable Integer id) {
        try {
            EtudiantDTO etudiant = etudiantService.getEtudiantById(id);
            return ResponseEntity.ok(etudiant);
        } catch (RuntimeException e) {
            logger.warning("Student not found: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Student not found: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Error getting student by id: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error: " + e.getMessage());
        }
    }
    
  

    @PostMapping
    public ResponseEntity<?> createEtudiant(@RequestBody EtudiantDTO etudiantDTO) {
        try {
            EtudiantDTO createdEtudiant = etudiantService.createEtudiant(etudiantDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEtudiant);
        } catch (Exception e) {
            logger.severe("Error creating student: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating student: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEtudiant(
            @PathVariable Integer id, 
            @RequestBody EtudiantDTO etudiantDTO) {
        try {
            EtudiantDTO updatedEtudiant = etudiantService.updateEtudiant(id, etudiantDTO);
            return ResponseEntity.ok(updatedEtudiant);
        } catch (RuntimeException e) {
            logger.warning("Error updating student: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Student not found: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Error updating student: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating student: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEtudiant(@PathVariable Integer id) {
        try {
            etudiantService.deleteEtudiant(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.warning("Error deleting student: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Student not found: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Error deleting student: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting student: " + e.getMessage());
        }
    }

    @GetMapping("/section/{section}")
    public ResponseEntity<?> getEtudiantsBySection(@PathVariable String section) {
        try {
            List<EtudiantDTO> etudiants = etudiantService.getEtudiantsBySection(section);
            return ResponseEntity.ok(etudiants);
        } catch (Exception e) {
            logger.severe("Error getting students by section: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error getting students by section: " + e.getMessage());
        }
    }

    @GetMapping("/groupe/{groupe}")
    public ResponseEntity<?> getEtudiantsByGroupe(@PathVariable String groupe) {
        try {
            List<EtudiantDTO> etudiants = etudiantService.getEtudiantsByGroupe(groupe);
            return ResponseEntity.ok(etudiants);
        } catch (Exception e) {
            logger.severe("Error getting students by group: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error getting students by group: " + e.getMessage());
        }
    }

    @GetMapping("/encadrant/{encadrantId}")
    public ResponseEntity<?> getEtudiantsByEncadrant(@PathVariable Integer encadrantId) {
        try {
            List<EtudiantDTO> etudiants = etudiantService.getEtudiantsByEncadrant(encadrantId);
            return ResponseEntity.ok(etudiants);
        } catch (Exception e) {
            logger.severe("Error getting students by supervisor: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error getting students by supervisor: " + e.getMessage());
        }
    }

    @GetMapping("/classement")
    public ResponseEntity<?> getClassementEtudiants() {
        try {
            List<EtudiantDTO> etudiants = etudiantService.getClassementEtudiants();
            return ResponseEntity.ok(etudiants);
        } catch (Exception e) {
            logger.severe("Error getting student ranking: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error getting student ranking: " + e.getMessage());
        }
    }
    
    @GetMapping("/nbEtudiant")
    public int nombreEtudiant() {
    	return etudiantService.nbreEtudiants();
    }
}