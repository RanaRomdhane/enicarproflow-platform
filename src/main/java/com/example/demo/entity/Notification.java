package com.example.demo.entity;

import java.time.LocalDateTime;

import org.apache.catalina.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private Integer userId; // ID de l'utilisateur (étudiant/enseignant)
    private String titre;
    private String message;
    private String lien;
    private LocalDateTime dateCreation;
    private boolean lue;
    
}
