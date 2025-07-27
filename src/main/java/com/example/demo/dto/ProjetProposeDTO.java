package com.example.demo.dto;



import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ProjetProposeDTO {
    private Integer id;
    private String titre;
    private String description;
    private String domaine;
    private String difficulte; // Notez le nom correspondant à l'entité
    private List<String> technologies;
    private String status;
    private LocalDate dateProposition;

    // Constructeurs
    public ProjetProposeDTO() {}

    public ProjetProposeDTO(Integer id, String titre, String description, String domaine, 
                          String difficulte, List<String> technologies, String status, 
                          LocalDate dateProposition) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.domaine = domaine;
        this.difficulte = difficulte;
        this.technologies = technologies;
        this.status = status;
        this.dateProposition = dateProposition;
    }

    // Getters et setters
    // ... (à générer avec Lombok ou manuellement)
}