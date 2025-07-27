package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Etudiant;
import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {
    
    Optional<Etudiant> findByEmail(String email);
    
    List<Etudiant> findBySection(String section);
    
    List<Etudiant> findByGroupe(String groupe);
    
    List<Etudiant> findByEncadrant_IdEnseignant(Integer encadrantId);
    
    List<Etudiant> findByOrderByMoyenneDesc();
    Optional<Etudiant> findByEmailAndMotDePasse(String email, String motDePasse);
    
    @Query("select count(e) from Etudiant e")
    int nbEtd();
    
    

}