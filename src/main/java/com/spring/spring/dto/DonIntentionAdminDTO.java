package com.spring.spring.dto;

import com.spring.spring.entity.DonIntention;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DonIntentionAdminDTO {

    private Long id;
    private String nomComplet;
    private String email;
    private String telephone;
    private BigDecimal montant;
    private String montantType;
    private String modePaiementSouhaite;
    private String message;
    private String statut;
    private LocalDateTime dateSoumission;
    private LocalDateTime dateContact;
    private LocalDateTime dateConversion;
    private String notesInternes;
    private String utmSource;
    private String utmMedium;
    private String utmCampaign;
    private String ipAddress;
    private String pays;
    private String ville;
    private LocalDateTime createdAt;

    public DonIntentionAdminDTO() {}

    public DonIntentionAdminDTO(DonIntention intention) {
        this.id = intention.getId();
        this.nomComplet = intention.getNomComplet();
        this.email = intention.getEmail();
        this.telephone = intention.getTelephone();
        this.montant = intention.getMontant();
        this.montantType = intention.getMontantType() != null ? intention.getMontantType().name() : null;
        this.modePaiementSouhaite = intention.getModePaiementSouhaite() != null ? intention.getModePaiementSouhaite().name() : null;
        this.message = intention.getMessage();
        this.statut = intention.getStatut().name();
        this.dateSoumission = intention.getDateSoumission();
        this.dateContact = intention.getDateContact();
        this.dateConversion = intention.getDateConversion();
        this.notesInternes = intention.getNotesInternes();
        this.utmSource = intention.getUtmSource();
        this.utmMedium = intention.getUtmMedium();
        this.utmCampaign = intention.getUtmCampaign();
        this.ipAddress = intention.getIpAddress();
        this.pays = intention.getPays();
        this.ville = intention.getVille();
        this.createdAt = intention.getCreatedAt();
    }

    // Getters et Setters (à générer automatiquement dans votre IDE)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public String getMontantType() { return montantType; }
    public void setMontantType(String montantType) { this.montantType = montantType; }

    public String getModePaiementSouhaite() { return modePaiementSouhaite; }
    public void setModePaiementSouhaite(String modePaiementSouhaite) { this.modePaiementSouhaite = modePaiementSouhaite; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(LocalDateTime dateSoumission) { this.dateSoumission = dateSoumission; }

    public LocalDateTime getDateContact() { return dateContact; }
    public void setDateContact(LocalDateTime dateContact) { this.dateContact = dateContact; }

    public LocalDateTime getDateConversion() { return dateConversion; }
    public void setDateConversion(LocalDateTime dateConversion) { this.dateConversion = dateConversion; }

    public String getNotesInternes() { return notesInternes; }
    public void setNotesInternes(String notesInternes) { this.notesInternes = notesInternes; }

    public String getUtmSource() { return utmSource; }
    public void setUtmSource(String utmSource) { this.utmSource = utmSource; }

    public String getUtmMedium() { return utmMedium; }
    public void setUtmMedium(String utmMedium) { this.utmMedium = utmMedium; }

    public String getUtmCampaign() { return utmCampaign; }
    public void setUtmCampaign(String utmCampaign) { this.utmCampaign = utmCampaign; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}