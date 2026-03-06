package com.spring.spring.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "temoignage")
public class Temoignage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auteur_fr", nullable = false)
    private String auteurFr;

    @Column(name = "auteur_en", nullable = false)
    private String auteurEn;

    @Column(name = "fonction_fr", nullable = false)
    private String fonctionFr;

    @Column(name = "fonction_en", nullable = false)
    private String fonctionEn;

    @Column(name = "contenu_fr", columnDefinition = "TEXT", nullable = false)
    private String contenuFr;

    @Column(name = "contenu_en", columnDefinition = "TEXT", nullable = false)
    private String contenuEn;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_temoignage")
    private TypeTemoignage typeTemoignage = TypeTemoignage.PHOTO;

    @Column(name = "date_publication", nullable = false)
    private LocalDate datePublication;

    @Column(name = "actif")
    private Boolean actif = true;

    @Column(name = "ordre_affichage")
    private Integer ordreAffichage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructeurs
    public Temoignage() {}

    public Temoignage(Long id, String auteurFr, String auteurEn, String fonctionFr,
                      String fonctionEn, String contenuFr, String contenuEn,
                      String photoUrl, String videoUrl, TypeTemoignage typeTemoignage,
                      LocalDate datePublication, Boolean actif,
                      Integer ordreAffichage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.auteurFr = auteurFr;
        this.auteurEn = auteurEn;
        this.fonctionFr = fonctionFr;
        this.fonctionEn = fonctionEn;
        this.contenuFr = contenuFr;
        this.contenuEn = contenuEn;
        this.photoUrl = photoUrl;
        this.videoUrl = videoUrl;
        this.typeTemoignage = typeTemoignage;
        this.datePublication = datePublication;
        this.actif = actif;
        this.ordreAffichage = ordreAffichage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getAuteurFr() { return auteurFr; }
    public String getAuteurEn() { return auteurEn; }
    public String getFonctionFr() { return fonctionFr; }
    public String getFonctionEn() { return fonctionEn; }
    public String getContenuFr() { return contenuFr; }
    public String getContenuEn() { return contenuEn; }
    public String getPhotoUrl() { return photoUrl; }
    public String getVideoUrl() { return videoUrl; }
    public TypeTemoignage getTypeTemoignage() { return typeTemoignage; }
    public LocalDate getDatePublication() { return datePublication; }
    public Boolean getActif() { return actif; }
    public Integer getOrdreAffichage() { return ordreAffichage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setAuteurFr(String auteurFr) { this.auteurFr = auteurFr; }
    public void setAuteurEn(String auteurEn) { this.auteurEn = auteurEn; }
    public void setFonctionFr(String fonctionFr) { this.fonctionFr = fonctionFr; }
    public void setFonctionEn(String fonctionEn) { this.fonctionEn = fonctionEn; }
    public void setContenuFr(String contenuFr) { this.contenuFr = contenuFr; }
    public void setContenuEn(String contenuEn) { this.contenuEn = contenuEn; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public void setTypeTemoignage(TypeTemoignage typeTemoignage) { this.typeTemoignage = typeTemoignage; }
    public void setDatePublication(LocalDate datePublication) { this.datePublication = datePublication; }
    public void setActif(Boolean actif) { this.actif = actif; }
    public void setOrdreAffichage(Integer ordreAffichage) { this.ordreAffichage = ordreAffichage; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum TypeTemoignage {
        PHOTO,      // Témoignage avec photo seulement
        VIDEO,      // Témoignage avec vidéo seulement
        PHOTO_VIDEO // Témoignage avec photo ET vidéo
    }
}