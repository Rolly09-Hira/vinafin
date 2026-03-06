package com.spring.spring.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "mission")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre_fr", nullable = false)
    private String titreFr;

    @Column(name = "titre_en", nullable = false)
    private String titreEn;

    @Column(name = "slogan_fr")
    private String sloganFr;

    @Column(name = "slogan_en")
    private String sloganEn;

    @Column(name = "description_fr", columnDefinition = "TEXT", nullable = false)
    private String descriptionFr;

    @Column(name = "description_en", columnDefinition = "TEXT", nullable = false)
    private String descriptionEn;

    @Column(name = "objectifs_fr", columnDefinition = "TEXT")
    private String objectifsFr;

    @Column(name = "objectifs_en", columnDefinition = "TEXT")
    private String objectifsEn;

    @Column(name = "valeurs_fr", columnDefinition = "TEXT")
    private String valeursFr;

    @Column(name = "valeurs_en", columnDefinition = "TEXT")
    private String valeursEn;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "ordre_affichage")
    private Integer ordreAffichage;

    @Column(name = "actif")
    private Boolean actif = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructeur par défaut
    public Mission() {}

    // Constructeur avec tous les champs
    public Mission(Long id, String titreFr, String titreEn, String sloganFr, String sloganEn,
                   String descriptionFr, String descriptionEn, String objectifsFr, String objectifsEn,
                   String valeursFr, String valeursEn, String iconUrl, String imageUrl,
                   Integer ordreAffichage, Boolean actif, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.titreFr = titreFr;
        this.titreEn = titreEn;
        this.sloganFr = sloganFr;
        this.sloganEn = sloganEn;
        this.descriptionFr = descriptionFr;
        this.descriptionEn = descriptionEn;
        this.objectifsFr = objectifsFr;
        this.objectifsEn = objectifsEn;
        this.valeursFr = valeursFr;
        this.valeursEn = valeursEn;
        this.iconUrl = iconUrl;
        this.imageUrl = imageUrl;
        this.ordreAffichage = ordreAffichage;
        this.actif = actif;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitreFr() { return titreFr; }
    public String getTitreEn() { return titreEn; }
    public String getSloganFr() { return sloganFr; }
    public String getSloganEn() { return sloganEn; }
    public String getDescriptionFr() { return descriptionFr; }
    public String getDescriptionEn() { return descriptionEn; }
    public String getObjectifsFr() { return objectifsFr; }
    public String getObjectifsEn() { return objectifsEn; }
    public String getValeursFr() { return valeursFr; }
    public String getValeursEn() { return valeursEn; }
    public String getIconUrl() { return iconUrl; }
    public String getImageUrl() { return imageUrl; }
    public Integer getOrdreAffichage() { return ordreAffichage; }
    public Boolean getActif() { return actif; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitreFr(String titreFr) { this.titreFr = titreFr; }
    public void setTitreEn(String titreEn) { this.titreEn = titreEn; }
    public void setSloganFr(String sloganFr) { this.sloganFr = sloganFr; }
    public void setSloganEn(String sloganEn) { this.sloganEn = sloganEn; }
    public void setDescriptionFr(String descriptionFr) { this.descriptionFr = descriptionFr; }
    public void setDescriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; }
    public void setObjectifsFr(String objectifsFr) { this.objectifsFr = objectifsFr; }
    public void setObjectifsEn(String objectifsEn) { this.objectifsEn = objectifsEn; }
    public void setValeursFr(String valeursFr) { this.valeursFr = valeursFr; }
    public void setValeursEn(String valeursEn) { this.valeursEn = valeursEn; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setOrdreAffichage(Integer ordreAffichage) { this.ordreAffichage = ordreAffichage; }
    public void setActif(Boolean actif) { this.actif = actif; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}