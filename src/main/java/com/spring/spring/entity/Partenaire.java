package com.spring.spring.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "partenaire")
public class Partenaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypePartenaire type;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "description_fr", columnDefinition = "TEXT")
    private String descriptionFr;

    @Column(name = "description_en", columnDefinition = "TEXT")
    private String descriptionEn;

    @Column(name = "site_web")
    private String siteWeb;

    @Column(name = "email")
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "date_debut_partenaire", nullable = false)
    private LocalDate dateDebutPartenaire;

    @Column(name = "actif")
    private Boolean actif = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructeurs
    public Partenaire() {}

    public Partenaire(Long id, String nom, TypePartenaire type, String logoUrl,
                      String descriptionFr, String descriptionEn, String siteWeb,
                      String email, String telephone, String adresse,
                      LocalDate dateDebutPartenaire, Boolean actif,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.logoUrl = logoUrl;
        this.descriptionFr = descriptionFr;
        this.descriptionEn = descriptionEn;
        this.siteWeb = siteWeb;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateDebutPartenaire = dateDebutPartenaire;
        this.actif = actif;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public TypePartenaire getType() { return type; }
    public String getLogoUrl() { return logoUrl; }
    public String getDescriptionFr() { return descriptionFr; }
    public String getDescriptionEn() { return descriptionEn; }
    public String getSiteWeb() { return siteWeb; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public String getAdresse() { return adresse; }
    public LocalDate getDateDebutPartenaire() { return dateDebutPartenaire; }
    public Boolean getActif() { return actif; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setType(TypePartenaire type) { this.type = type; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public void setDescriptionFr(String descriptionFr) { this.descriptionFr = descriptionFr; }
    public void setDescriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }
    public void setEmail(String email) { this.email = email; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setDateDebutPartenaire(LocalDate dateDebutPartenaire) { this.dateDebutPartenaire = dateDebutPartenaire; }
    public void setActif(Boolean actif) { this.actif = actif; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum TypePartenaire {
        entreprise,
        individu,
        association,
        institution
    }
}