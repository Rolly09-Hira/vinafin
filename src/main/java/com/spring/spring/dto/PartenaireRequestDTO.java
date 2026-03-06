package com.spring.spring.dto;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class PartenaireRequestDTO {
    private String nom;
    private String type;
    private MultipartFile logoFile;
    private String descriptionFr;
    private String descriptionEn;
    private String siteWeb;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDate dateDebutPartenaire;
    private Boolean actif;

    // Getters
    public String getNom() { return nom; }
    public String getType() { return type; }
    public MultipartFile getLogoFile() { return logoFile; }
    public String getDescriptionFr() { return descriptionFr; }
    public String getDescriptionEn() { return descriptionEn; }
    public String getSiteWeb() { return siteWeb; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public String getAdresse() { return adresse; }
    public LocalDate getDateDebutPartenaire() { return dateDebutPartenaire; }
    public Boolean getActif() { return actif; }

    // Setters
    public void setNom(String nom) { this.nom = nom; }
    public void setType(String type) { this.type = type; }
    public void setLogoFile(MultipartFile logoFile) { this.logoFile = logoFile; }
    public void setDescriptionFr(String descriptionFr) { this.descriptionFr = descriptionFr; }
    public void setDescriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }
    public void setEmail(String email) { this.email = email; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setDateDebutPartenaire(LocalDate dateDebutPartenaire) { this.dateDebutPartenaire = dateDebutPartenaire; }
    public void setActif(Boolean actif) { this.actif = actif; }
}