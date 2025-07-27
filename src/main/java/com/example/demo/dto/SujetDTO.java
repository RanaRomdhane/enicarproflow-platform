package com.example.demo.dto;

import lombok.Data;
import java.util.List;


@Data
public class SujetDTO {
    private Integer id;
    private String titre;
    private String description;
    private String domaine;
    private String difficulte;
    private List<String> technologies;
    private String encadrantNom;
    private Boolean estValide;
}