package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.entity.Administrateur;
import com.example.demo.dto.EtudiantDTO;
import com.example.demo.dto.EnseignantDTO;
import com.example.demo.services.EtudiantService;
import com.example.demo.services.EnseignantService;
import com.example.demo.services.AdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private EtudiantService etudiantService;
    
    @Autowired
    private EnseignantService enseignantService;
    
    @Autowired
    private AdministrateurService administrateurService;
    
    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getMotDePasse() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email and password are required");
            }
            
            // Check if student (Etudiant)
            EtudiantDTO etudiant = etudiantService.authenticate(
                loginRequest.getEmail(), loginRequest.getMotDePasse()
            );
            
            if (etudiant != null) {
                // If student, return their ID and type
                return ResponseEntity.ok(new LoginResponse(etudiant.getId(), "student"));
            }
            
            // Check if teacher (Enseignant)
            EnseignantDTO enseignant = enseignantService.authenticate(
                loginRequest.getEmail(), loginRequest.getMotDePasse()
            );
            
            if (enseignant != null) {
                // If teacher, return their ID and type
                return ResponseEntity.ok(new LoginResponse(enseignant.getId(), "teacher"));
            }
            
            // Check if admin (Administrateur)
            Administrateur admin = administrateurService.authenticate(
                loginRequest.getEmail(), loginRequest.getMotDePasse()
            );
            
            if (admin != null) {
                // If admin, return their ID and type
                // Assuming director is the same as admin in your system
                return ResponseEntity.ok(new LoginResponse(admin.getIdAdmin(), "director"));
            }
            
            // If no user matches, return Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (Exception e) {
            logger.error("Login error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Login error: " + e.getMessage());
        }
    }
}