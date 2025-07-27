package com.example.demo.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "affectation")
@Data
public class Affectation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // correspond à la colonne `dateAffectation` (type DATE)
    @Column(name = "dateAffectation", nullable = false)
    private LocalDate dateAffectation;

    // correspond à la colonne sujet_id (clé étrangère vers la table sujet)
    @OneToOne
    @JoinColumn(name = "sujet_id", nullable = false)
    @JsonIgnore
    private Sujet sujet;

    // correspond à la colonne etudiant1_idEtudiant (clé étrangère vers etudiant.idEtudiant)
    @ManyToOne
    @JoinColumn(name = "etudiant1_idEtudiant")
    @JsonIgnore
    private Etudiant etudiant1;

    // correspond à la colonne etudiant2_idEtudiant (clé étrangère vers etudiant.idEtudiant)
    @ManyToOne
    @JoinColumn(name = "etudiant2_idEtudiant")
    @JsonIgnore
    private Etudiant etudiant2;

    // correspond à la colonne encadrant_enseignant (clé étrangère vers enseignant.id)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = " encadrant_idEnseignant")
    @JsonIgnore
    private Enseignant encadrant;
}
