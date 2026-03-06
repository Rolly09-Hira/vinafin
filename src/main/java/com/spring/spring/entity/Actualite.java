package com.spring.spring.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "actualite")
public class Actualite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre_fr", nullable = false)
    private String titreFr;

    @Column(name = "titre_en", nullable = false)
    private String titreEn;

    @Column(name = "contenu_fr", columnDefinition = "TEXT", nullable = false)
    private String contenuFr;

    @Column(name = "contenu_en", columnDefinition = "TEXT", nullable = false)
    private String contenuEn;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeActualite type;

    @Column(name = "date_publication", nullable = false)
    private LocalDate datePublication;

    @Column(name = "date_evenement")
    private LocalDate dateEvenement;

    @Column(name = "lieu")
    private String lieu;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "important")
    private Boolean important = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructeurs
    public Actualite() {}

    public Actualite(Long id, String titreFr, String titreEn, String contenuFr,
                     String contenuEn, TypeActualite type, LocalDate datePublication,
                     LocalDate dateEvenement, String lieu, String imageUrl,
                     Boolean important, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.titreFr = titreFr;
        this.titreEn = titreEn;
        this.contenuFr = contenuFr;
        this.contenuEn = contenuEn;
        this.type = type;
        this.datePublication = datePublication;
        this.dateEvenement = dateEvenement;
        this.lieu = lieu;
        this.imageUrl = imageUrl;
        this.important = important;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitreFr() { return titreFr; }
    public String getTitreEn() { return titreEn; }
    public String getContenuFr() { return contenuFr; }
    public String getContenuEn() { return contenuEn; }
    public TypeActualite getType() { return type; }
    public LocalDate getDatePublication() { return datePublication; }
    public LocalDate getDateEvenement() { return dateEvenement; }
    public String getLieu() { return lieu; }
    public String getImageUrl() { return imageUrl; }
    public Boolean getImportant() { return important; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitreFr(String titreFr) { this.titreFr = titreFr; }
    public void setTitreEn(String titreEn) { this.titreEn = titreEn; }
    public void setContenuFr(String contenuFr) { this.contenuFr = contenuFr; }
    public void setContenuEn(String contenuEn) { this.contenuEn = contenuEn; }
    public void setType(TypeActualite type) { this.type = type; }
    public void setDatePublication(LocalDate datePublication) { this.datePublication = datePublication; }
    public void setDateEvenement(LocalDate dateEvenement) { this.dateEvenement = dateEvenement; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setImportant(Boolean important) { this.important = important; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum TypeActualite {
        evenement,
        nouvelle,
        rapport
    }
}