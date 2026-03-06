package com.spring.spring.config;

import com.spring.spring.entity.Utilisateur;
import com.spring.spring.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        // Créer un admin par défaut si aucun utilisateur n'existe
        if (utilisateurRepository.count() == 0) {
            // Admin
            Utilisateur admin = new Utilisateur();
            admin.setNom("Administrateur");
            admin.setEmail("admin@vina.org");
            admin.setMotDePasse(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setPhotoUrl("");
            admin.setActif(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            utilisateurRepository.save(admin);
            System.out.println("=========================================");
            System.out.println("ADMIN CRÉÉ:");
            System.out.println("Email: admin@vina.org");
            System.out.println("Mot de passe: admin123");

            // Éditeur
            Utilisateur editeur = new Utilisateur();
            editeur.setNom("Éditeur Test");
            editeur.setEmail("editeur@vina.org");
            editeur.setMotDePasse(passwordEncoder.encode("editeur123"));
            editeur.setRole("EDITEUR");
            editeur.setPhotoUrl("");
            editeur.setActif(true);
            editeur.setCreatedAt(LocalDateTime.now());
            editeur.setUpdatedAt(LocalDateTime.now());

            utilisateurRepository.save(editeur);
            System.out.println("ÉDITEUR CRÉÉ:");
            System.out.println("Email: editeur@vina.org");
            System.out.println("Mot de passe: editeur123");
            System.out.println("=========================================");
        }

    }
}