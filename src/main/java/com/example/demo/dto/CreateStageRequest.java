package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.entity.StageStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CreateStageRequest {
    private String entreprise;
    private String projet;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateFin;

    private String lieu;
    private String encadrantIndustriel;
    private String emailEncadrant;
    private String description;

    // Bind enum as JSON string
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private StageStatus status;

    // Just the IDs, not full objects
    private Integer etudiantId;
}

