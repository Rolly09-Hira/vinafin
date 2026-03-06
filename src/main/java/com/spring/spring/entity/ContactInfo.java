package com.spring.spring.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "contact_info")
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre", nullable = false, unique = true)
    private String titre; // Ex: "Adresse", "Téléphone", "Email", "Facebook", "WhatsApp"

    @Column(name = "valeur", nullable = false, columnDefinition = "TEXT")
    private String valeur; // La valeur du contact

    @Column(name = "icone")
    private String icone; // Nom de l'icône FontAwesome ou autre (ex: "fa-phone", "fa-envelope")

    @Column(name = "type_contact", nullable = false)
    private String typeContact; // "telephone", "email", "adresse", "reseau_social"

    @Column(name = "lien")
    private String lien; // URL pour les réseaux sociaux

    @Column(name = "ordre_affichage")
    private Integer ordreAffichage; // Pour ordonner l'affichage

    @Column(name = "actif")
    private Boolean actif = true;

    // Constructeurs
    public ContactInfo() {}

    public ContactInfo(String titre, String valeur, String typeContact) {
        this.titre = titre;
        this.valeur = valeur;
        this.typeContact = typeContact;
        this.actif = true;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public String getValeur() { return valeur; }
    public String getIcone() { return icone; }
    public String getTypeContact() { return typeContact; }
    public String getLien() { return lien; }
    public Integer getOrdreAffichage() { return ordreAffichage; }
    public Boolean getActif() { return actif; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setValeur(String valeur) { this.valeur = valeur; }
    public void setIcone(String icone) { this.icone = icone; }
    public void setTypeContact(String typeContact) { this.typeContact = typeContact; }
    public void setLien(String lien) { this.lien = lien; }
    public void setOrdreAffichage(Integer ordreAffichage) { this.ordreAffichage = ordreAffichage; }
    public void setActif(Boolean actif) { this.actif = actif; }
}