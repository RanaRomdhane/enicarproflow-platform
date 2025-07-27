package com.example.demo.dto;

import lombok.Data;
import java.util.List;

import com.example.demo.entity.Enseignant;

@Data
public class EnseignantDTO {
    private Integer id;
    private String prenom;
    private String nom;
    private String email;
    private String departement;
    private String position;
    private String bureau;
    private Integer nombreEtudiants;

    public EnseignantDTO(Integer id, String prenom, String nom, String email, String departement,
            String position, String bureau, Integer nombreEtudiants) {
this.id = id;
this.prenom = prenom;
this.nom = nom;
this.email = email;
this.departement = departement;
this.position = position;
this.bureau = bureau;
this.nombreEtudiants = nombreEtudiants;
}



}
