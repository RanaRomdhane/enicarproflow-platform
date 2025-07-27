package com.example.demo.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sujet")
@Data
public class Sujet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titre;
    private String description;
    private String domaine;
    private String difficulte;
    private LocalDate dateProposition;
    private Boolean estValide;
    private Boolean estPropose;

    @ElementCollection
    private List<String> technologies;

    
    @ManyToOne
    @JsonIgnore
    private Enseignant encadrant;

    @OneToOne(mappedBy = "sujet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Affectation affectation;
}
