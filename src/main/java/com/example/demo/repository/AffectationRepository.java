package com.example.demo.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Affectation;
import com.example.demo.entity.Etudiant;


public interface AffectationRepository extends JpaRepository<Affectation, Long> {
    
    // Vérifie si un étudiant est déjà affecté (en tant qu'étudiant1 OU étudiant2)
    boolean existsByEtudiant1OrEtudiant2(Etudiant etudiant1, Etudiant etudiant2);
    
    // Trouver toutes les affectations d'un encadrant
    List<Affectation> findByEncadrant_IdEnseignant(Integer encadrantId);
    
    // Trouver les affectations d'un étudiant
    @Query("SELECT a FROM Affectation a WHERE a.etudiant1.id = :etudiantId OR a.etudiant2.id = :etudiantId")
    List<Affectation> findByEtudiantId(@Param("etudiantId") Integer etudiantId);
    
 // Nouvelles méthodes nécessaires
    Optional<Affectation> findByEtudiant1_IdEtudiantOrEtudiant2_IdEtudiant(Long etudiant1Id, Long etudiant2Id);
    
    // Si vous avez besoin de plus de flexibilité
    @Query("SELECT a FROM Affectation a WHERE a.etudiant1.id = :etudiantId OR a.etudiant2.id = :etudiantId")
    Optional<Affectation> trouverParEtudiant(@Param("etudiantId") Long etudiantId);
}