package com.spring.spring.dto;

import jakarta.validation.constraints.NotBlank;

public class ContactInfoRequestDTO {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "La valeur est obligatoire")
    private String valeur;

    private String icone;

    @NotBlank(message = "Le type de contact est obligatoire")
    private String typeContact; // "telephone", "email", "adresse", "reseau_social"

    private String lien;

    private Integer ordreAffichage;

    private Boolean actif = true;

    // Getters
    public String getTitre() { return titre; }
    public String getValeur() { return valeur; }
    public String getIcone() { return icone; }
    public String getTypeContact() { return typeContact; }
    public String getLien() { return lien; }
    public Integer getOrdreAffichage() { return ordreAffichage; }
    public Boolean getActif() { return actif; }

    // Setters
    public void setTitre(String titre) { this.titre = titre; }
    public void setValeur(String valeur) { this.valeur = valeur; }
    public void setIcone(String icone) { this.icone = icone; }
    public void setTypeContact(String typeContact) { this.typeContact = typeContact; }
    public void setLien(String lien) { this.lien = lien; }
    public void setOrdreAffichage(Integer ordreAffichage) { this.ordreAffichage = ordreAffichage; }
    public void setActif(Boolean actif) { this.actif = actif; }
}