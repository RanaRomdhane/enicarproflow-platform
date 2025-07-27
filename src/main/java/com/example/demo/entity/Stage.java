package com.example.demo.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.*;


@Entity
@Table(name = "stage", uniqueConstraints = {@UniqueConstraint(columnNames = {"etudiant_id", "dateDebut", "dateFin"})})
@Data
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String entreprise;
    private String projet;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String lieu;
    private String encadrantIndustriel;
    private String emailEncadrant;
    private String description;
    
    @Convert(converter = StageStatus.StageStatusConverter.class)
    private StageStatus status;
    
    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;
  
    
    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

}

