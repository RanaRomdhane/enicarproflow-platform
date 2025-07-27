package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "document")
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDocument;
    
    private String nom;
    private LocalDate dateUpload;
    private String cheminFichier;
    private String typeFichier; // pdf, docx, etc.
    
    @Enumerated(EnumType.STRING)
    private TypeDocument type; // RAPPORT, POSTER, ATTESTATION
    
    private String status; // Validé, En attente, Rejeté
    
    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;
    
    
   
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_stage", nullable = true)
    private Stage stage;
}