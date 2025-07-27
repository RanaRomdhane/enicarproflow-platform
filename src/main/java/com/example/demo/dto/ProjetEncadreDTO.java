package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjetEncadreDTO {
    public ProjetEncadreDTO(Integer id2, String titre2, String etudiant, LocalDate dateDebut2, LocalDate dateFin2, Integer progress2) {
		// TODO Auto-generated constructor stub
	}
	private Integer id;
    private String titre;
    private String etudiant;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer progress;
    
}