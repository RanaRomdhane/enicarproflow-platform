package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dto.ProjetEncadreDTO;
import com.example.demo.entity.Enseignant;
import com.example.demo.entity.Etudiant;
import com.example.demo.entity.Sujet;


public interface EnseignantRepository extends JpaRepository<Enseignant, Integer> {
	// Trouver un enseignant par son email
    Enseignant findByEmail(String email);
    Optional<Enseignant> findByEmailAndMotDePasse(String email, String motDePasse);


    // Trouver tous les enseignants d’un département donné (si applicable)
    List<Enseignant> findByDepartement(String departement);
	
    @Query("SELECT COUNT(e) FROM Etudiant e WHERE e.encadrant.idEnseignant = :idEnseignant")
    int countEtudiantsEncadres(Integer idEnseignant);
    
    // Trouver les enseignants avec moins de X étudiants encadrés (pour l'affectation automatique)
    @Query("SELECT ens FROM Enseignant ens WHERE SIZE(ens.etudiantsEncadres) < :maxCapacity")
    List<Enseignant> findAvailableEnseignants(int maxCapacity);
    
    // Vérifier l'existence d'un email
    boolean existsByEmail(String email);
    //@Query("SELECT p FROM Sujet p WHERE p.encadrant.idEnseignant = :enseignantId")
   // List<Sujet> findByEncadrant_IdEnseignant(Integer idEnseignant);
    
    

    @Query("SELECT e FROM Etudiant e WHERE e.encadrant.idEnseignant = :enseignantId")
    List<Etudiant> findEtudiantsEncadres(Integer enseignantId);
    
    
}
