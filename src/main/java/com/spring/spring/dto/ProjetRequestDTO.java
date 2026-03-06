package com.spring.spring.dto;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class ProjetRequestDTO {
    private String titreFr;
    private String titreEn;
    private String descriptionFr;
    private String descriptionEn;
    private String objectifFr;
    private String objectifEn;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String statut;
    private MultipartFile imageFile;
    private String domaineFr;
    private String domaineEn;

    // 🌟 NOUVEAUX CHAMPS
    private Long regionId;        // ID de la région (FK)
    private Integer beneficiaires; // Nombre de bénéficiaires

    // Getters
    public String getTitreFr() { return titreFr; }
    public String getTitreEn() { return titreEn; }
    public String getDescriptionFr() { return descriptionFr; }
    public String getDescriptionEn() { return descriptionEn; }
    public String getObjectifFr() { return objectifFr; }
    public String getObjectifEn() { return objectifEn; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public String getStatut() { return statut; }
    public MultipartFile getImageFile() { return imageFile; }
    public String getDomaineFr() { return domaineFr; }
    public String getDomaineEn() { return domaineEn; }

    // 🌟 NOUVEAUX GETTERS
    public Long getRegionId() { return regionId; }
    public Integer getBeneficiaires() { return beneficiaires; }

    // Setters
    public void setTitreFr(String titreFr) { this.titreFr = titreFr; }
    public void setTitreEn(String titreEn) { this.titreEn = titreEn; }
    public void setDescriptionFr(String descriptionFr) { this.descriptionFr = descriptionFr; }
    public void setDescriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; }
    public void setObjectifFr(String objectifFr) { this.objectifFr = objectifFr; }
    public void setObjectifEn(String objectifEn) { this.objectifEn = objectifEn; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setImageFile(MultipartFile imageFile) { this.imageFile = imageFile; }
    public void setDomaineFr(String domaineFr) { this.domaineFr = domaineFr; }
    public void setDomaineEn(String domaineEn) { this.domaineEn = domaineEn; }

    // 🌟 NOUVEAUX SETTERS
    public void setRegionId(Long regionId) { this.regionId = regionId; }
    public void setBeneficiaires(Integer beneficiaires) { this.beneficiaires = beneficiaires; }
}