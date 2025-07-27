package com.example.demo.dto;



import lombok.Data;
import java.time.LocalDate;

@Data
public class StageDTO {
    private Integer id;
    private String etudiant;
    private String entreprise;
    private String projet;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String status;
    private String reportStatus;
}