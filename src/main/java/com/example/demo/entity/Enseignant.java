package com.example.demo.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "enseignant")
@Data
public class Enseignant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEnseignant;

    private String prenom;
    private String nom;
    private String email;

    @JsonIgnore
    private String motDePasse;

    private String departement;
    private String position;
    private String bureau;

    @OneToMany(mappedBy = "encadrant")
    @JsonIgnore
    private List<Etudiant> etudiantsEncadres;

    @OneToMany(mappedBy = "encadrant")
    @JsonIgnore
    private List<Sujet> sujetsEncadres;

    public String getNomComplet() {
        return prenom + " " + nom;
    }
}
