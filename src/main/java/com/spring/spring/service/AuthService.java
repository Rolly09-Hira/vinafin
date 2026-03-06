package com.spring.spring.service;

import com.spring.spring.dto.UtilisateurRequestDTO;
import com.spring.spring.entity.Utilisateur;
import com.spring.spring.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private FileStorageService fileStorageService;

    @Transactional
    public Utilisateur creerUtilisateur(UtilisateurRequestDTO dto) {
        if (utilisateurRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        utilisateur.setRole(dto.getRole());
        utilisateur.setActif(true);
        utilisateur.setCreatedAt(LocalDateTime.now());
        utilisateur.setUpdatedAt(LocalDateTime.now());

        // Gérer l'upload de la photo si présente
        if (dto.getPhotoFile() != null && !dto.getPhotoFile().isEmpty()) {
            String photoUrl = fileStorageService.storeProfilePhoto(dto.getPhotoFile());
            utilisateur.setPhotoUrl(photoUrl);
        }

        return utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public Utilisateur updateUserWithPhoto(Long id, String nom, String email, String role, Boolean actif, MultipartFile photoFile) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String oldEmail = utilisateur.getEmail();

        if (nom != null && !nom.trim().isEmpty()) {
            utilisateur.setNom(nom.trim());
        }

        if (email != null && !email.trim().isEmpty() && !email.equals(utilisateur.getEmail())) {
            if (utilisateurRepository.existsByEmail(email)) {
                throw new RuntimeException("Cet email est déjà utilisé");
            }
            utilisateur.setEmail(email.trim());
        }

        if (role != null && (role.equals("ADMIN") || role.equals("EDITEUR"))) {
            utilisateur.setRole(role);
        }

        if (actif != null) {
            utilisateur.setActif(actif);
        }

        // Gérer l'upload de la nouvelle photo
        if (photoFile != null && !photoFile.isEmpty()) {
            // Supprimer l'ancienne photo si elle existe
            if (utilisateur.getPhotoUrl() != null && !utilisateur.getPhotoUrl().isEmpty()) {
                fileStorageService.deleteFile(utilisateur.getPhotoUrl());
            }
            String newPhotoUrl = fileStorageService.storeProfilePhoto(photoFile);
            utilisateur.setPhotoUrl(newPhotoUrl);
        }

        utilisateur.setUpdatedAt(LocalDateTime.now());
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    @Transactional
    public void changerMotDePasse(String email, String nouveauMotDePasse) {
        Utilisateur utilisateur = findByEmail(email);
        utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateur.setUpdatedAt(LocalDateTime.now());
        utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public void desactiverUtilisateur(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        utilisateur.setActif(false);
        utilisateur.setUpdatedAt(LocalDateTime.now());
        utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public Utilisateur updateProfile(Long id, String nom, String email, MultipartFile photoFile) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (nom != null && !nom.trim().isEmpty()) {
            utilisateur.setNom(nom.trim());
        }

        if (email != null && !email.trim().isEmpty() && !email.equals(utilisateur.getEmail())) {
            if (utilisateurRepository.existsByEmail(email)) {
                throw new RuntimeException("Cet email est déjà utilisé");
            }
            utilisateur.setEmail(email.trim());
        }

        // Gérer l'upload de la nouvelle photo
        if (photoFile != null && !photoFile.isEmpty()) {
            // Supprimer l'ancienne photo si elle existe
            if (utilisateur.getPhotoUrl() != null) {
                fileStorageService.deleteFile(utilisateur.getPhotoUrl());
            }
            String newPhotoUrl = fileStorageService.storeProfilePhoto(photoFile);
            utilisateur.setPhotoUrl(newPhotoUrl);
        }

        utilisateur.setUpdatedAt(LocalDateTime.now());
        return utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public Utilisateur updateUser(Long id, String nom, String email, String role, Boolean actif) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (nom != null && !nom.trim().isEmpty()) {
            utilisateur.setNom(nom.trim());
        }

        if (email != null && !email.trim().isEmpty() && !email.equals(utilisateur.getEmail())) {
            if (utilisateurRepository.existsByEmail(email)) {
                throw new RuntimeException("Cet email est déjà utilisé");
            }
            utilisateur.setEmail(email.trim());
        }

        if (role != null && (role.equals("ADMIN") || role.equals("EDITEUR"))) {
            utilisateur.setRole(role);
        }

        if (actif != null) {
            utilisateur.setActif(actif);
        }

        utilisateur.setUpdatedAt(LocalDateTime.now());
        return utilisateurRepository.save(utilisateur);
    }
}