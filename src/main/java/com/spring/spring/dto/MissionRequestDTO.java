package com.spring.spring.dto;

import org.springframework.web.multipart.MultipartFile;

public class MissionRequestDTO {
    private String titreFr;
    private String titreEn;
    private String sloganFr;
    private String sloganEn;
    private String descriptionFr;
    private String descriptionEn;
    private String objectifsFr;
    private String objectifsEn;
    private String valeursFr;
    private String valeursEn;
    private MultipartFile iconFile;
    private MultipartFile imageFile;
    private Integer ordreAffichage;
    private Boolean actif;

    // Getters
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
    public MultipartFile getIconFile() { return iconFile; }
    public MultipartFile getImageFile() { return imageFile; }
    public Integer getOrdreAffichage() { return ordreAffichage; }
    public Boolean getActif() { return actif; }

    // Setters
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
    public void setIconFile(MultipartFile iconFile) { this.iconFile = iconFile; }
    public void setImageFile(MultipartFile imageFile) { this.imageFile = imageFile; }
    public void setOrdreAffichage(Integer ordreAffichage) { this.ordreAffichage = ordreAffichage; }
    public void setActif(Boolean actif) { this.actif = actif; }
}