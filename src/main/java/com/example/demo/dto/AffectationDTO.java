package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.entity.Affectation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AffectationDTO {
    private Integer id;
    private String etudiant1;
    private String etudiant2;
    private String sujet;
    private String enseignant;
    private LocalDate dateAffectation;
    
    
    public static AffectationDTO fromEntity(Affectation affectation) {
        AffectationDTO dto = new AffectationDTO();
        dto.setId(affectation.getId());
        dto.setEtudiant1(affectation.getEtudiant1().getPrenom() + " " + affectation.getEtudiant1().getNom());
        if(affectation.getEtudiant2() != null) {
            dto.setEtudiant2(affectation.getEtudiant2().getPrenom() + " " + affectation.getEtudiant2().getNom());
        }
        dto.setSujet(affectation.getSujet().getTitre());
        dto.setEnseignant(affectation.getEncadrant().getPrenom() + " " + affectation.getEncadrant().getNom());
        dto.setDateAffectation(affectation.getDateAffectation());
        return dto;
    }

	public AffectationDTO() {
		// TODO Auto-generated constructor stub
	}

	public AffectationDTO(Integer id, String etudiant1, String etudiant2, String sujet, String enseignant,
			LocalDate dateAffectation) {
		super();
		this.id = id;
		this.etudiant1 = etudiant1;
		this.etudiant2 = etudiant2;
		this.sujet = sujet;
		this.enseignant = enseignant;
		this.dateAffectation = dateAffectation;
	}

	
}
