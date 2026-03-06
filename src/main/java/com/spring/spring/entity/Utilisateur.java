// MODIFIER l'entité Utilisateur
package com.spring.spring.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column(name = "role", nullable = false)
    private String role; // "ADMIN" ou "EDITEUR"

    @Column(name = "photo_url")
    private String photoUrl; // NOUVEAU CHAMP

    @Column(name = "actif")
    private Boolean actif = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructeurs
    public Utilisateur() {}

    public Utilisateur(Long id, String nom, String email, String motDePasse,
                       String role, String photoUrl, Boolean actif,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.photoUrl = photoUrl;
        this.actif = actif;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters et Setters (ajouter pour photoUrl)
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public String getRole() { return role; }
    public String getPhotoUrl() { return photoUrl; } // NOUVEAU GETTER
    public Boolean getActif() { return actif; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setEmail(String email) { this.email = email; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public void setRole(String role) { this.role = role; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; } // NOUVEAU SETTER
    public void setActif(Boolean actif) { this.actif = actif; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}