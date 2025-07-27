package com.example.demo.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "administrateur")
@Data
public class Administrateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAdmin;
    
    @Column(nullable = false)
    private String prenom;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String motDePasse;
    
    private String departement; // "Informatique", "Génie Industriel", etc.
    private String anneeUniversitaire; // Ex: "2024-2025"
    
    
    
   
    
    // Méthodes utilitaires
    public String getNomComplet() {
        return prenom + " " + nom;
    }
}