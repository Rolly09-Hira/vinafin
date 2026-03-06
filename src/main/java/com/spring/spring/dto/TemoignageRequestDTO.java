package com.spring.spring.dto;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class TemoignageRequestDTO {
    private String auteurFr;
    private String auteurEn;
    private String fonctionFr;
    private String fonctionEn;
    private String contenuFr;
    private String contenuEn;
    private MultipartFile photoFile;
    private MultipartFile videoFile;
    private String typeTemoignage;
    private LocalDate datePublication;
    private Boolean actif;
    private Integer ordreAffichage;

    // Getters
    public String getAuteurFr() { return auteurFr; }
    public String getAuteurEn() { return auteurEn; }
    public String getFonctionFr() { return fonctionFr; }
    public String getFonctionEn() { return fonctionEn; }
    public String getContenuFr() { return contenuFr; }
    public String getContenuEn() { return contenuEn; }
    public MultipartFile getPhotoFile() { return photoFile; }
    public MultipartFile getVideoFile() { return videoFile; }
    public String getTypeTemoignage() { return typeTemoignage; }
    public LocalDate getDatePublication() { return datePublication; }
    public Boolean getActif() { return actif; }
    public Integer getOrdreAffichage() { return ordreAffichage; }

    // Setters
    public void setAuteurFr(String auteurFr) { this.auteurFr = auteurFr; }
    public void setAuteurEn(String auteurEn) { this.auteurEn = auteurEn; }
    public void setFonctionFr(String fonctionFr) { this.fonctionFr = fonctionFr; }
    public void setFonctionEn(String fonctionEn) { this.fonctionEn = fonctionEn; }
    public void setContenuFr(String contenuFr) { this.contenuFr = contenuFr; }
    public void setContenuEn(String contenuEn) { this.contenuEn = contenuEn; }
    public void setPhotoFile(MultipartFile photoFile) { this.photoFile = photoFile; }
    public void setVideoFile(MultipartFile videoFile) { this.videoFile = videoFile; }
    public void setTypeTemoignage(String typeTemoignage) { this.typeTemoignage = typeTemoignage; }
    public void setDatePublication(LocalDate datePublication) { this.datePublication = datePublication; }
    public void setActif(Boolean actif) { this.actif = actif; }
    public void setOrdreAffichage(Integer ordreAffichage) { this.ordreAffichage = ordreAffichage; }
}