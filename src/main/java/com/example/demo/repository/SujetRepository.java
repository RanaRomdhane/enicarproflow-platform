package com.example.demo.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Sujet;


public interface SujetRepository extends JpaRepository<Sujet, Integer> {
    
    // Trouver les sujets validés et non proposés par les étudiants
    List<Sujet> findByEstValideTrueAndEstProposeFalse();
    
    // Trouver les sujets proposés par un enseignant
    List<Sujet> findByEncadrant_IdEnseignant(Integer encadrantId);
    
    // Trouver les sujets validés
    List<Sujet> findByEstValideTrue();
    
    // Trouver les sujets proposés par les étudiants
    List<Sujet> findByEstProposeTrue();
    
    
      // Remplacer l'ancienne méthode par :
    @Query("""
            SELECT s
              FROM Sujet s
             WHERE s.estValide = true
               AND NOT EXISTS (
                 SELECT 1
                   FROM Affectation a
                  WHERE a.sujet = s
               )
            """)      List<Sujet> findSujetsValidesNonAffectes();
    
      List<Sujet> findByEncadrant_IdEnseignantAndAffectationIsNotNull(Integer enseignantId);
      List<Sujet> findByEstValideIsNull();
      
      // Find all subjects where EstValide is true (validated)
      List<Sujet> findByEstValideIsTrue();
      
      // Find all subjects where EstValide is false (rejected)
      List<Sujet> findByEstValideIsFalse();
      
      @Query("select count(s) from Sujet s")
      int nbSujet();
      
      @Query("select count(s) from Sujet s where s.estValide=true")
      int nbSujetsValides();
      
      @Query("select count(s) from Sujet s where s.estValide=NULL")
      int nbSujetsEnAttente();
      
      @Query("SELECT COUNT(DISTINCT s.encadrant) FROM Sujet s WHERE s.encadrant IS NOT NULL")
      int nbEnseignants();
      
      @Query("select count(s) from Sujet s WHERE s.encadrant.idEnseignant = :idEnseignant")
      int nbSujetEncadres(@Param("idEnseignant")long idEnseignant);
      
      @Query("select count(s) from Sujet s WHERE s.encadrant.idEnseignant = :idEnseignant and s.estValide=true")
      int nbSujetEncadresValides(@Param("idEnseignant")long idEnseignant);
      
      @Query("select count(s) from Sujet s WHERE s.encadrant.idEnseignant = :idEnseignant and s.estValide=null")
      int nbSujetEncadresEnAttente(@Param("idEnseignant")long idEnseignant);
      
  }






