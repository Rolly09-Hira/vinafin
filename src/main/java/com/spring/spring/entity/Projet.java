package com.spring.spring.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "projet")
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre_fr", nullable = false)
    private String titreFr;

    @Column(name = "titre_en", nullable = false)
    private String titreEn;

    @Column(name = "description_fr", columnDefinition = "TEXT", nullable = false)
    private String descriptionFr;

    @Column(name = "description_en", columnDefinition = "TEXT", nullable = false)
    private String descriptionEn;

    @Column(name = "objectif_fr", nullable = false)
    private String objectifFr;

    @Column(name = "objectif_en", nullable = false)
    private String objectifEn;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutProjet statut;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "domaine_fr", nullable = false)
    private String domaineFr;

    @Column(name = "domaine_en", nullable = false)
    private String domaineEn;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "beneficiaires")
    private Integer beneficiaires;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructeurs
    public Projet() {}

    public Projet(Long id, String titreFr, String titreEn, String descriptionFr,
                  String descriptionEn, String objectifFr, String objectifEn,
                  LocalDate dateDebut, LocalDate dateFin, StatutProjet statut,
                  String imageUrl, String domaineFr, String domaineEn,
                  Region region, Integer beneficiaires,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.titreFr = titreFr;
        this.titreEn = titreEn;
        this.descriptionFr = descriptionFr;
        this.descriptionEn = descriptionEn;
        this.objectifFr = objectifFr;
        this.objectifEn = objectifEn;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.imageUrl = imageUrl;
        this.domaineFr = domaineFr;
        this.domaineEn = domaineEn;
        this.region = region;
        this.beneficiaires = beneficiaires;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters et Setters existants + nouveaux
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitreFr() { return titreFr; }
    public void setTitreFr(String titreFr) { this.titreFr = titreFr; }

    public String getTitreEn() { return titreEn; }
    public void setTitreEn(String titreEn) { this.titreEn = titreEn; }

    public String getDescriptionFr() { return descriptionFr; }
    public void setDescriptionFr(String descriptionFr) { this.descriptionFr = descriptionFr; }

    public String getDescriptionEn() { return descriptionEn; }
    public void setDescriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; }

    public String getObjectifFr() { return objectifFr; }
    public void setObjectifFr(String objectifFr) { this.objectifFr = objectifFr; }

    public String getObjectifEn() { return objectifEn; }
    public void setObjectifEn(String objectifEn) { this.objectifEn = objectifEn; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public StatutProjet getStatut() { return statut; }
    public void setStatut(StatutProjet statut) { this.statut = statut; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDomaineFr() { return domaineFr; }
    public void setDomaineFr(String domaineFr) { this.domaineFr = domaineFr; }

    public String getDomaineEn() { return domaineEn; }
    public void setDomaineEn(String domaineEn) { this.domaineEn = domaineEn; }

    // 🌟 NOUVEAUX GETTERS/SETTERS
    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }

    public Integer getBeneficiaires() { return beneficiaires; }
    public void setBeneficiaires(Integer beneficiaires) { this.beneficiaires = beneficiaires; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum StatutProjet {
        en_cours,
        termine,
        a_venir,
        suspendu
    }
}