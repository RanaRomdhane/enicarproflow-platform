package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "choixEtudiant") 
@Entity
@Data
public class ChoixEtudiant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    private Etudiant etudiant;
    
    @ManyToOne
    private Sujet sujet;
    
    private Integer ordrePreference; // 1 à 5
    
    @ManyToOne
    private Etudiant binome; // Null si solo
    
    private boolean estPropose; // True si sujet proposé par l'étudiant
}