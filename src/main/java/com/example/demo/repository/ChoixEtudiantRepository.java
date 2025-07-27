package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.ChoixEtudiant;
import com.example.demo.entity.Etudiant;
import com.example.demo.entity.Sujet;

public interface ChoixEtudiantRepository extends JpaRepository<ChoixEtudiant, Integer> {
    
    // Trouver les choix d'un étudiant spécifique
	 List<ChoixEtudiant> findByEtudiant_IdEtudiant(Integer idEtudiant);
    
    // Trouver les choix pour un sujet, triés par ordre de préférence
    List<ChoixEtudiant> findBySujetOrderByOrdrePreference(Sujet sujet);
    
    // Trouver les propositions de sujets faites par les étudiants
    List<ChoixEtudiant> findByEstProposeTrue();
    
    // Vérifier si un étudiant a déjà choisi un sujet spécifique
    boolean existsByEtudiantAndSujet(Etudiant etudiant, Sujet sujet);
    
    @Query("select count(DISTINCT c.etudiant) from ChoixEtudiant c WHERE c.etudiant IS NOT NULL")
    int nbEtudiantsAyantChoisis();
}
