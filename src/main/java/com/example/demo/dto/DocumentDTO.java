package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DocumentDTO {
    private Integer id;
    private String nom;
    private String type;
    private String status;
    private LocalDate dateUpload;
    private Integer etudiantId;
    private Integer stageId;
    private String nomFichier;
    
}