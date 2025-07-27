package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Stage;
import com.example.demo.entity.StageStatus;


public interface StageRepository extends JpaRepository<Stage, Integer> {
    List<Stage> findByStatus(StageStatus status);
    List<Stage> findByEtudiant_IdEtudiant(Integer etudiantId);
    
    @Query("select count(s) from Stage s")
    int nombreStage();
    
}
