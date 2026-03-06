package com.spring.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class PersonnelRequestDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;

    private String telephone;

    @NotBlank(message = "Le poste est obligatoire")
    private String poste;

    private String departement;

    @PastOrPresent(message = "La date d'embauche ne peut pas être dans le futur")
    private LocalDate dateEmbauche;

    private String biographieFr;

    private String biographieEn;

    private String specialites;

    private MultipartFile photoFile;

    private String linkedinUrl;

    private String twitterUrl;

    private String facebookUrl;

    private Integer ordreAffichage;

    // Getters et Setters
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public String getPoste() { return poste; }
    public String getDepartement() { return departement; }
    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public String getBiographieFr() { return biographieFr; }
    public String getBiographieEn() { return biographieEn; }
    public String getSpecialites() { return specialites; }
    public MultipartFile getPhotoFile() { return photoFile; }
    public String getLinkedinUrl() { return linkedinUrl; }
    public String getTwitterUrl() { return twitterUrl; }
    public String getFacebookUrl() { return facebookUrl; }
    public Integer getOrdreAffichage() { return ordreAffichage; }

    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setPoste(String poste) { this.poste = poste; }
    public void setDepartement(String departement) { this.departement = departement; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }
    public void setBiographieFr(String biographieFr) { this.biographieFr = biographieFr; }
    public void setBiographieEn(String biographieEn) { this.biographieEn = biographieEn; }
    public void setSpecialites(String specialites) { this.specialites = specialites; }
    public void setPhotoFile(MultipartFile photoFile) { this.photoFile = photoFile; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }
    public void setTwitterUrl(String twitterUrl) { this.twitterUrl = twitterUrl; }
    public void setFacebookUrl(String facebookUrl) { this.facebookUrl = facebookUrl; }
    public void setOrdreAffichage(Integer ordreAffichage) { this.ordreAffichage = ordreAffichage; }
}