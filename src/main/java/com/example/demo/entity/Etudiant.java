package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "etudiant")
@Data
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEtudiant;

    private String prenom;
    private String nom;
    private String email;

    @JsonIgnore
    private String motDePasse;

    private String section;
    private String groupe;
    private Double moyenne;
    private Integer ordreMerite;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_enseignant")
    @JsonIgnore
    private Enseignant encadrant;

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL,orphanRemoval=true)
    @JsonIgnore
    private List<Stage> stage= new ArrayList();

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Document> documents;

    @OneToMany(mappedBy = "etudiant1")
    @JsonIgnore
    private List<Affectation> affectationsCommeEtudiant1;

    @OneToMany(mappedBy = "etudiant2")
    @JsonIgnore
    private List<Affectation> affectationsCommeEtudiant2;

    public String getNomComplet() {
        return prenom + " " + nom;
    }
}
