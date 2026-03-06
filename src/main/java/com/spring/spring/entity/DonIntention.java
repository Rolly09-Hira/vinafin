package com.spring.spring.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "don_intentions")
public class DonIntention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_complet", nullable = false, length = 100)
    private String nomComplet;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "telephone", nullable = false, length = 20)
    private String telephone;

    @Column(name = "montant", precision = 12, scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(name = "montant_type", length = 20)
    private MontantType montantType;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_paiement_souhaite", length = 30)
    private ModePaiement modePaiementSouhaite;

    @Column(name = "message", length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 20)
    private StatutDon statut = StatutDon.EN_ATTENTE;

    @Column(name = "date_soumission", nullable = false)
    private LocalDateTime dateSoumission = LocalDateTime.now();

    @Column(name = "date_contact")
    private LocalDateTime dateContact;

    @Column(name = "date_conversion")
    private LocalDateTime dateConversion;

    @Column(name = "notes_internes", length = 2000)
    private String notesInternes;

    @Column(name = "utm_source", length = 50)
    private String utmSource;

    @Column(name = "utm_medium", length = 50)
    private String utmMedium;

    @Column(name = "utm_campaign", length = 50)
    private String utmCampaign;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "pays", length = 50)
    private String pays;

    @Column(name = "ville", length = 100)
    private String ville;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructeurs
    public DonIntention() {}

    public DonIntention(Long id, String nomComplet, String email, String telephone,
                        BigDecimal montant, MontantType montantType,
                        ModePaiement modePaiementSouhaite, String message,
                        StatutDon statut, LocalDateTime dateSoumission,
                        LocalDateTime dateContact, LocalDateTime dateConversion,
                        String notesInternes, String utmSource, String utmMedium,
                        String utmCampaign, String ipAddress, String userAgent,
                        String pays, String ville, LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        this.id = id;
        this.nomComplet = nomComplet;
        this.email = email;
        this.telephone = telephone;
        this.montant = montant;
        this.montantType = montantType;
        this.modePaiementSouhaite = modePaiementSouhaite;
        this.message = message;
        this.statut = statut;
        this.dateSoumission = dateSoumission;
        this.dateContact = dateContact;
        this.dateConversion = dateConversion;
        this.notesInternes = notesInternes;
        this.utmSource = utmSource;
        this.utmMedium = utmMedium;
        this.utmCampaign = utmCampaign;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.pays = pays;
        this.ville = ville;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getNomComplet() { return nomComplet; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public BigDecimal getMontant() { return montant; }
    public MontantType getMontantType() { return montantType; }
    public ModePaiement getModePaiementSouhaite() { return modePaiementSouhaite; }
    public String getMessage() { return message; }
    public StatutDon getStatut() { return statut; }
    public LocalDateTime getDateSoumission() { return dateSoumission; }
    public LocalDateTime getDateContact() { return dateContact; }
    public LocalDateTime getDateConversion() { return dateConversion; }
    public String getNotesInternes() { return notesInternes; }
    public String getUtmSource() { return utmSource; }
    public String getUtmMedium() { return utmMedium; }
    public String getUtmCampaign() { return utmCampaign; }
    public String getIpAddress() { return ipAddress; }
    public String getUserAgent() { return userAgent; }
    public String getPays() { return pays; }
    public String getVille() { return ville; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }
    public void setEmail(String email) { this.email = email; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public void setMontantType(MontantType montantType) { this.montantType = montantType; }
    public void setModePaiementSouhaite(ModePaiement modePaiementSouhaite) { this.modePaiementSouhaite = modePaiementSouhaite; }
    public void setMessage(String message) { this.message = message; }
    public void setStatut(StatutDon statut) { this.statut = statut; }
    public void setDateSoumission(LocalDateTime dateSoumission) { this.dateSoumission = dateSoumission; }
    public void setDateContact(LocalDateTime dateContact) { this.dateContact = dateContact; }
    public void setDateConversion(LocalDateTime dateConversion) { this.dateConversion = dateConversion; }
    public void setNotesInternes(String notesInternes) { this.notesInternes = notesInternes; }
    public void setUtmSource(String utmSource) { this.utmSource = utmSource; }
    public void setUtmMedium(String utmMedium) { this.utmMedium = utmMedium; }
    public void setUtmCampaign(String utmCampaign) { this.utmCampaign = utmCampaign; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public void setPays(String pays) { this.pays = pays; }
    public void setVille(String ville) { this.ville = ville; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Méthodes utilitaires
    public void marquerCommeContacte() {
        this.statut = StatutDon.CONTACTE;
        this.dateContact = LocalDateTime.now();
    }

    public void marquerCommeConverti() {
        this.statut = StatutDon.CONVERTI;
        this.dateConversion = LocalDateTime.now();
    }

    public void marquerCommePerdu() {
        this.statut = StatutDon.PERDU;
    }

    public void marquerCommeReporte() {
        this.statut = StatutDon.REPORTE;
    }

    public boolean estEnAttente() {
        return StatutDon.EN_ATTENTE.equals(this.statut);
    }

    public boolean estContacte() {
        return StatutDon.CONTACTE.equals(this.statut);
    }

    public boolean estConverti() {
        return StatutDon.CONVERTI.equals(this.statut);
    }

    // Enums internes (comme dans votre exemple)
    public enum StatutDon {
        EN_ATTENTE,
        CONTACTE,
        CONVERTI,
        PERDU,
        REPORTE
    }

    public enum ModePaiement {
        WAVE,
        ORANGE_MONEY,
        FREE_MONEY,
        VIREMENT,
        CHEQUE,
        ESPECES,
        AUTRE
    }

    public enum MontantType {
        FIXE,
        LIBRE
    }
}