package com.example.demo.dto;

import lombok.Data;

@Data
public class EtudiantDTO {
    private Integer id;
    private String prenom;
    private String nom;
    private String email;
    private String motDePasse;
    private String section;
    private String groupe;
    private Double moyenne;
    private Integer ordreMerite;
    private Integer encadrantId;
    private String encadrantNom;
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }
}