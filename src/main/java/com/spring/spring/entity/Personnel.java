package com.spring.spring.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "personnel")
public class Personnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "poste", nullable = false)
    private String poste;

    @Column(name = "departement")
    private String departement;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;

    @Column(name = "biographie_fr", columnDefinition = "TEXT")
    private String biographieFr;

    @Column(name = "biographie_en", columnDefinition = "TEXT")
    private String biographieEn;

    @Column(name = "specialites")
    private String specialites;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "twitter_url")
    private String twitterUrl;

    @Column(name = "facebook_url")
    private String facebookUrl;

    @Column(name = "ordre_affichage")
    private Integer ordreAffichage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructeurs
    public Personnel() {}

    public Personnel(Long id, String nom, String prenom, String email, String telephone,
                     String poste, String departement, LocalDate dateEmbauche,
                     String biographieFr, String biographieEn, String specialites,
                     String photoUrl, String linkedinUrl, String twitterUrl, String facebookUrl,
                     Integer ordreAffichage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.poste = poste;
        this.departement = departement;
        this.dateEmbauche = dateEmbauche;
        this.biographieFr = biographieFr;
        this.biographieEn = biographieEn;
        this.specialites = specialites;
        this.photoUrl = photoUrl;
        this.linkedinUrl = linkedinUrl;
        this.twitterUrl = twitterUrl;
        this.facebookUrl = facebookUrl;
        this.ordreAffichage = ordreAffichage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public String getPoste() { return poste; }
    public String getDepartement() { return departement; }
    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public String getBiographieFr() { return biographieFr; }
    public String getBiographieEn() { return biographieEn; }
    public String getSpecialites() { return specialites; }
    public String getPhotoUrl() { return photoUrl; }
    public String getLinkedinUrl() { return linkedinUrl; }
    public String getTwitterUrl() { return twitterUrl; }
    public String getFacebookUrl() { return facebookUrl; }
    public Integer getOrdreAffichage() { return ordreAffichage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setPoste(String poste) { this.poste = poste; }
    public void setDepartement(String departement) { this.departement = departement; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }
    public void setBiographieFr(String biographieFr) { this.biographieFr = biographieFr; }
    public void setBiographieEn(String biographieEn) { this.biographieEn = biographieEn; }
    public void setSpecialites(String specialites) { this.specialites = specialites; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }
    public void setTwitterUrl(String twitterUrl) { this.twitterUrl = twitterUrl; }
    public void setFacebookUrl(String facebookUrl) { this.facebookUrl = facebookUrl; }
    public void setOrdreAffichage(Integer ordreAffichage) { this.ordreAffichage = ordreAffichage; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}