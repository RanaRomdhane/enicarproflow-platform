package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProjetProposeDTO;
import com.example.demo.entity.Sujet;
import com.example.demo.services.SujetService;

@RestController
@RequestMapping("/api/projets")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {

    @Autowired
    private SujetService sujetService;
    @PostMapping("/proposer")
    public ResponseEntity<ProjetProposeDTO> proposerProjet(@RequestBody ProjetProposeDTO projetDTO) {
        try {
            Sujet sujet = new Sujet();
            sujet.setTitre(projetDTO.getTitre());
            sujet.setDescription(projetDTO.getDescription());
            sujet.setDomaine(projetDTO.getDomaine());
            sujet.setDifficulte(projetDTO.getDifficulte());
            sujet.setTechnologies(projetDTO.getTechnologies());
            // Marquer comme en attente (null)
            sujet.setEstValide(null);

            LocalDate dateProp = projetDTO.getDateProposition() != null
                    ? projetDTO.getDateProposition()
                    : LocalDate.now();
            sujet.setDateProposition(dateProp);

            Sujet saved = sujetService.saveSujet(sujet);

            ProjetProposeDTO responseDTO = new ProjetProposeDTO(
                    saved.getId(),
                    saved.getTitre(),
                    saved.getDescription(),
                    saved.getDomaine(),
                    saved.getDifficulte(),
                    saved.getTechnologies(),
                    "En attente",
                    saved.getDateProposition()
            );

            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);}
        }
    /**
     * Récupère tous les sujets (déjà transformés en DTO par le service)
     */
    @GetMapping
    public List<ProjetProposeDTO> getAllProjets() {
        return sujetService.getAllSujets();
    }

    /**
     * Récupère les sujets validés et non affectés
     */
    @GetMapping("/valides")
    public List<ProjetProposeDTO> getValidesNonAffectes() {
        return sujetService.getSujetsValidesNonAffectes();
    }
    
    @GetMapping("/nbSujet")
    public int nombreDeSujet() {
    	return sujetService.sujetsProposes();
    }
    
    @GetMapping("/nbSujetValide")
    public int nombreDeSujetValide() {
    	return sujetService.sujetsValides();
    }
    
    @GetMapping("/nbSujetEnAttente")
    public int nombreDeSujetEnAttente() {
    	return sujetService.sujetsEnAtt();
    }
    
    @GetMapping("/nbEnseignant")
    public int nombreDEnseignant() {
    	return sujetService.nbEns();
    }
    
    @GetMapping("/enseignant/{idEnseignant}/nbSujetsEncadres")
    public int getNombreSujetsEncadres(@PathVariable long idEnseignant) {
        return sujetService.nbSujetParEncadant(idEnseignant);
    }
    
    
    
}