package com.example.demo.services;

import com.example.demo.entity.ChoixEtudiant;
import com.example.demo.entity.Etudiant;
import com.example.demo.entity.Sujet;
import com.example.demo.repository.ChoixEtudiantRepository;
import com.example.demo.repository.EtudiantRepository;
import com.example.demo.repository.SujetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChoixEtudiantService {

    private final ChoixEtudiantRepository choixEtudiantRepository;
    private final EtudiantRepository etudiantRepository;
    private final SujetRepository sujetRepository;

    @Autowired
    public ChoixEtudiantService(
            ChoixEtudiantRepository choixEtudiantRepository,
            EtudiantRepository etudiantRepository,
            SujetRepository sujetRepository) {
        this.choixEtudiantRepository = choixEtudiantRepository;
        this.etudiantRepository = etudiantRepository;
        this.sujetRepository = sujetRepository;
    }

    public ChoixEtudiant createChoixEtudiant(ChoixEtudiant choixEtudiant) {
        // Récupérer l'étudiant existant
        Optional<Etudiant> etudiantOptional = etudiantRepository.findById(choixEtudiant.getEtudiant().getIdEtudiant());
        if (!etudiantOptional.isPresent()) {
            throw new RuntimeException("Etudiant non trouvé");
        }
        Etudiant etudiant = etudiantOptional.get();
        
        // Récupérer le sujet existant
        Optional<Sujet> sujetOptional = sujetRepository.findById(choixEtudiant.getSujet().getId());
        if (!sujetOptional.isPresent()) {
            throw new RuntimeException("Sujet non trouvé");
        }
        Sujet sujet = sujetOptional.get();

        // Associer l'étudiant et le sujet au choix
        choixEtudiant.setEtudiant(etudiant);
        choixEtudiant.setSujet(sujet);

        // Sauvegarder le choix étudiant
        return choixEtudiantRepository.save(choixEtudiant);
    }

    public List<ChoixEtudiant> findByEtudiantId(Integer idEtudiant) {
        return choixEtudiantRepository.findByEtudiant_IdEtudiant(idEtudiant);
    }
    
    public int nbreEtudiant() {
    	return choixEtudiantRepository.nbEtudiantsAyantChoisis();
    }
}
