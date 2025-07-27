package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Affectation;
import com.example.demo.entity.Etudiant;
import com.example.demo.entity.Notification;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.EnseignantRepository; // Assurez-vous d'avoir ce repository
import com.example.demo.repository.EtudiantRepository; // Assurez-vous d'avoir ce repository

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepo;
    private final EnseignantRepository enseignantRepo; // Pour récupérer l'email de l'enseignant
    private final EtudiantRepository etudiantRepo; // Pour récupérer l'email de l'étudiant

    public void notifierAffectation(Affectation affectation) {
        List<String> destinataires = new ArrayList<>();

        // Récupérer l'email de l'étudiant principal
        etudiantRepo.findById(affectation.getEtudiant1().getIdEtudiant())
                .ifPresent(etudiant -> {
                    destinataires.add(etudiant.getEmail());
                    creerNotification(
                            affectation.getEtudiant1().getIdEtudiant(),
                            "Affectation de projet",
                            "Vous avez été affecté au projet: " + affectation.getSujet().getTitre(),
                            "/projets/" + affectation.getSujet().getId()
                    );
                });

        // Récupérer l'email du binôme (si existe)
        if (affectation.getEtudiant2() != null) {
            etudiantRepo.findById(affectation.getEtudiant2().getIdEtudiant())
                    .ifPresent(etudiant -> {
                        destinataires.add(etudiant.getEmail());
                        creerNotification(
                                affectation.getEtudiant2().getIdEtudiant(),
                                "Affectation de projet en binôme",
                                "Vous avez été affecté au projet " + affectation.getSujet().getTitre() +
                                        " avec " + affectation.getEtudiant1().getPrenom(),
                                "/projets/" + affectation.getSujet().getId()
                        );
                    });
        }

        // Récupérer l'email de l'encadrant
        if (affectation.getEncadrant() != null) {
            enseignantRepo.findById(affectation.getEncadrant().getIdEnseignant())
                    .ifPresent(enseignant -> {
                        destinataires.add(enseignant.getEmail());
                        creerNotification(
                                affectation.getEncadrant().getIdEnseignant(),
                                "Nouvelle affectation",
                                "Nouveaux étudiants affectés à votre projet: " + affectation.getSujet().getTitre(),
                                "/encadrement/" + affectation.getSujet().getId()
                        );
                    });
        }

        // Envoyer un email unique à tous les destinataires
        if (!destinataires.isEmpty()) {
            envoyerEmail(destinataires, "Nouvelle affectation de projet", construireMessageAffectation(affectation));
        }
    }

    private String construireMessageAffectation(Affectation affectation) {
        StringBuilder message = new StringBuilder("Bonjour,\n\n");
        message.append("Vous êtes concerné par l'affectation suivante :\n\n");
        message.append("Projet : ").append(affectation.getSujet().getTitre()).append("\n");
        message.append("Description : ").append(affectation.getSujet().getDescription()).append("\n\n");
        message.append("Étudiant principal : ").append(affectation.getEtudiant1().getPrenom()).append(" ").append(affectation.getEtudiant1().getNom()).append("\n");
        if (affectation.getEtudiant2() != null) {
            message.append("Binôme : ").append(affectation.getEtudiant2().getPrenom()).append(" ").append(affectation.getEtudiant2().getNom()).append("\n");
        }
        if (affectation.getEncadrant() != null) {
            message.append("Encadrant : Dr. ").append(affectation.getEncadrant().getPrenom()).append(" ").append(affectation.getEncadrant().getNom()).append("\n");
        } else {
            message.append("Encadrant : Non assigné\n");
        }
        message.append("\nCordialement,\nL'équipe de gestion des projets");
        return message.toString();
    }

    private void creerNotification(Integer userId, String titre, String message, String lien) {
        // Enregistrement en base
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitre(titre);
        notification.setMessage(message);
        notification.setLien(lien);
        notification.setDateCreation(LocalDateTime.now());
        notification.setLue(false);
        notificationRepo.save(notification);
    }

    private void envoyerEmail(List<String> destinataires, String titre, String message) {
    	log.info("Envoi d'email à : {}", destinataires);
    	SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(destinataires.toArray(new String[0])); // Convertir la liste en tableau de Strings
        mail.setSubject(titre);
        mail.setText(message);
        mailSender.send(mail);
    }

    public void notifierSuppressionAffectation(Affectation affectation) {
        List<String> destinataires = new ArrayList<>();
        String projetTitre = affectation.getSujet().getTitre();
        String messageEtudiant = "Votre affectation au projet '" + projetTitre + "' a été annulée.";
        String messageBinome = "Votre affectation au projet '" + projetTitre + "' avec "
                + affectation.getEtudiant1().getPrenom() + " a été annulée.";
        String messageEncadrant = "L'affectation des étudiants au projet '" + projetTitre + "' a été annulée.";

        // Notification et ajout email étudiant principal
        etudiantRepo.findById(affectation.getEtudiant1().getIdEtudiant())
                .ifPresent(etudiant -> {
                    destinataires.add(etudiant.getEmail());
                    creerNotification(
                            affectation.getEtudiant1().getIdEtudiant(),
                            "Annulation d'affectation",
                            messageEtudiant,
                            "/projets/" + affectation.getSujet().getId()
                    );
                });

        // Notification et ajout email binôme (si existe)
        if (affectation.getEtudiant2() != null) {
            etudiantRepo.findById(affectation.getEtudiant2().getIdEtudiant())
                    .ifPresent(etudiant -> {
                        destinataires.add(etudiant.getEmail());
                        creerNotification(
                                affectation.getEtudiant2().getIdEtudiant(),
                                "Annulation d'affectation en binôme",
                                messageBinome,
                                "/projets/" + affectation.getSujet().getId()
                        );
                    });
        }

        // Notification et ajout email encadrant
        if (affectation.getEncadrant() != null) {
            enseignantRepo.findById(affectation.getEncadrant().getIdEnseignant())
                    .ifPresent(enseignant -> {
                        destinataires.add(enseignant.getEmail());
                        creerNotification(
                                affectation.getEncadrant().getIdEnseignant(),
                                "Annulation d'affectation",
                                messageEncadrant,
                                "/encadrement/" + affectation.getSujet().getId()
                        );
                    });
        }

        // Envoyer un email unique à tous les destinataires concernés par l'annulation
        if (!destinataires.isEmpty()) {
            envoyerEmail(destinataires, "Annulation d'affectation de projet", construireMessageAnnulation(affectation));
        }
    }

    private String construireMessageAnnulation(Affectation affectation) {
        StringBuilder message = new StringBuilder("Bonjour,\n\n");
        message.append("L'affectation suivante a été annulée :\n\n");
        message.append("Projet : ").append(affectation.getSujet().getTitre()).append("\n");
        message.append("Étudiant principal : ").append(affectation.getEtudiant1().getPrenom()).append(" ").append(affectation.getEtudiant1().getNom()).append("\n");
        if (affectation.getEtudiant2() != null) {
            message.append("Binôme : ").append(affectation.getEtudiant2().getPrenom()).append(" ").append(affectation.getEtudiant2().getNom()).append("\n");
        }
        if (affectation.getEncadrant() != null) {
            message.append("Encadrant : Dr. ").append(affectation.getEncadrant().getPrenom()).append(" ").append(affectation.getEncadrant().getNom()).append("\n");
        } else {
            message.append("Encadrant : Non assigné\n");
        }
        message.append("\nCordialement,\nL'équipe de gestion des projets");
        return message.toString();
    }
}