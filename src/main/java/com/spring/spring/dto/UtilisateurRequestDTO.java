package com.spring.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class UtilisateurRequestDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit avoir au moins 6 caractères")
    private String motDePasse;

    @NotBlank(message = "Le rôle est obligatoire")
    private String role;

    private MultipartFile photoFile; // NOUVEAU CHAMP

    // Getters et Setters
    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public String getRole() { return role; }
    public MultipartFile getPhotoFile() { return photoFile; } // NOUVEAU GETTER

    public void setNom(String nom) { this.nom = nom; }
    public void setEmail(String email) { this.email = email; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public void setRole(String role) { this.role = role; }
    public void setPhotoFile(MultipartFile photoFile) { this.photoFile = photoFile; } // NOUVEAU SETTER
}