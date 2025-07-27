package com.example.demo.dto;

import com.example.demo.entity.Administrateur;

import lombok.Data;

@Data
public class AdministrateurDTO {
    private Integer id;
    private String prenom;
    private String nom;
    private String email;
    private String departement;

    public static AdministrateurDTO fromEntity(Administrateur admin) {
        AdministrateurDTO dto = new AdministrateurDTO();
        dto.setId(admin.getIdAdmin());
        dto.setPrenom(admin.getPrenom());
        dto.setNom(admin.getNom());
        dto.setEmail(admin.getEmail());
        return dto;
    }
}