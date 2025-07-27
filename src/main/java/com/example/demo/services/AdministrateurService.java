package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Administrateur;
import com.example.demo.repository.AdministrateurRepository;

@Service
public class AdministrateurService {
	 @Autowired
	    private AdministrateurRepository administrateurRepository;

	    public List<Administrateur> getAllAdministrateurs() {
	        return administrateurRepository.findAll();
	    }
	    public Administrateur authenticate(String email, String motDePasse) {
	        Optional<Administrateur> admin = administrateurRepository.findByEmailAndMotDePasse(email, motDePasse);
	        return admin.orElse(null);
	    }

	    public Optional<Administrateur> getAdministrateurById(Integer id) {
	        return administrateurRepository.findById(id);
	    }

	    public Administrateur saveAdministrateur(Administrateur administrateur) {
	        return administrateurRepository.save(administrateur);
	    }

	    public void deleteAdministrateur(Integer id) {
	        administrateurRepository.deleteById(id);
	    }
	    public Optional<Administrateur> getAdminByMail(String email){
	    	return administrateurRepository.findByEmail(email);
	    }
	    public  Optional<Administrateur> getAdminByNames(String nom, String prenom){
	    	return administrateurRepository.findByNomAndPrenom( nom, prenom);
	    }
	    
	   

}
