package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProjetProposeDTO;
import com.example.demo.entity.Administrateur;
import com.example.demo.entity.Sujet;
import com.example.demo.services.AdministrateurService;
import com.example.demo.services.SujetService;

/**
 * Contrôleur REST pour la gestion administrative des sujets de projets.
 */
@RestController
@RequestMapping("/api/administrateurs")
@CrossOrigin(origins = "http://localhost:3000")
public class AdministrateurController {

    @Autowired
    private SujetService sujetService;

    @Autowired
    private AdministrateurService administrateurService;
    /**
     * Récupère tous les sujets en attente de validation.
     */
    @GetMapping("/en-attente")
    public ResponseEntity<List<ProjetProposeDTO>> getSujetsEnAttente() {
        List<ProjetProposeDTO> sujetsEnAttente = sujetService.getSujetsEnAttente();
        return new ResponseEntity<>(sujetsEnAttente, HttpStatus.OK);
    }

    /**
     * Récupère tous les sujets validés.
     */
    @GetMapping("/valides")
    public ResponseEntity<List<ProjetProposeDTO>> getSujetsValides() {
        List<ProjetProposeDTO> sujetsValides = sujetService.getSujetsValides();
        return new ResponseEntity<>(sujetsValides, HttpStatus.OK);
    }

    /**
     * Récupère tous les sujets rejetés.
     */
    @GetMapping("/rejetes")
    public ResponseEntity<List<ProjetProposeDTO>> getSujetsRejetes() {
        List<ProjetProposeDTO> sujetsRejetes = sujetService.getSujetsRejetes();
        return new ResponseEntity<>(sujetsRejetes, HttpStatus.OK);
    }

    /**
     * Valide un sujet spécifique.
     */
    @PatchMapping("/{id}/valider")
    public ResponseEntity<?> validerSujet(@PathVariable Integer id) {
        try {
            Sujet sujetValide = sujetService.validerSujet(id);
            ProjetProposeDTO dto = convertToDTO(sujetValide);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Une erreur est survenue lors de la validation du sujet", 
                                        HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Rejette un sujet spécifique.
     */
    @PatchMapping("/{id}/rejeter")
    public ResponseEntity<?> rejeterSujet(@PathVariable Integer id) {
        try {
            Sujet sujetRejete = sujetService.rejeterSujet(id);
            ProjetProposeDTO dto = convertToDTO(sujetRejete);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Une erreur est survenue lors du rejet du sujet", 
                                        HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Convertit une entité Sujet en ProjetProposeDTO.
     * Cette méthode reproduit la logique du convertToDTO dans SujetService
     * pour assurer la cohérence.
     */
    private ProjetProposeDTO convertToDTO(Sujet sujet) {
        String status;
        if (sujet.getEstValide() == null) {
            status = "En attente";
        } else if (sujet.getEstValide()) {
            status = "Validé";
        } else {
            status = "Rejeté";
        }
        return new ProjetProposeDTO(
                sujet.getId(),
                sujet.getTitre(),
                sujet.getDescription(),
                sujet.getDomaine(),
                sujet.getDifficulte(),
                sujet.getTechnologies(),
                status,
                sujet.getDateProposition()
        );
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Administrateur> getAdminInfo(@PathVariable Integer id) {
        Optional<Administrateur> administrateur = administrateurService.getAdministrateurById(id);
        if (administrateur.isPresent()) {
            return new ResponseEntity<>(administrateur.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}