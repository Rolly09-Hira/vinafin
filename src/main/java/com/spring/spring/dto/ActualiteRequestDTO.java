package com.spring.spring.dto;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class ActualiteRequestDTO {
    private String titreFr;
    private String titreEn;
    private String contenuFr;
    private String contenuEn;
    private String type;
    private LocalDate datePublication;
    private LocalDate dateEvenement;
    private String lieu;
    private MultipartFile imageFile;
    private Boolean important;

    // Getters
    public String getTitreFr() { return titreFr; }
    public String getTitreEn() { return titreEn; }
    public String getContenuFr() { return contenuFr; }
    public String getContenuEn() { return contenuEn; }
    public String getType() { return type; }
    public LocalDate getDatePublication() { return datePublication; }
    public LocalDate getDateEvenement() { return dateEvenement; }
    public String getLieu() { return lieu; }
    public MultipartFile getImageFile() { return imageFile; }
    public Boolean getImportant() { return important; }

    // Setters
    public void setTitreFr(String titreFr) { this.titreFr = titreFr; }
    public void setTitreEn(String titreEn) { this.titreEn = titreEn; }
    public void setContenuFr(String contenuFr) { this.contenuFr = contenuFr; }
    public void setContenuEn(String contenuEn) { this.contenuEn = contenuEn; }
    public void setType(String type) { this.type = type; }
    public void setDatePublication(LocalDate datePublication) { this.datePublication = datePublication; }
    public void setDateEvenement(LocalDate dateEvenement) { this.dateEvenement = dateEvenement; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public void setImageFile(MultipartFile imageFile) { this.imageFile = imageFile; }
    public void setImportant(Boolean important) { this.important = important; }
}