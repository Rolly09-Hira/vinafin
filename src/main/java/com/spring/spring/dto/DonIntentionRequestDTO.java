package com.spring.spring.dto;

import com.spring.spring.entity.DonIntention;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class DonIntentionRequestDTO {

    @NotBlank(message = "Le nom complet est requis")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nomComplet;

    @NotBlank(message = "L'email est requis")
    @Email(message = "Format d'email invalide")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    private String email;

    @NotBlank(message = "Le téléphone est requis")
    @Size(min = 8, max = 20, message = "Le téléphone doit contenir entre 8 et 20 caractères")
    @Pattern(
            regexp = "^[+]?[0-9\\s\\-()]{8,20}$",
            message = "Format de téléphone invalide. Utilisez un format international ex: +261771234567"
    )
    private String telephone;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être positif")
    @DecimalMax(value = "100000000", message = "Le montant ne peut pas dépasser 100 000 000 Ar")
    private BigDecimal montant;

    private DonIntention.MontantType montantType;

    private DonIntention.ModePaiement modePaiementSouhaite;

    @Size(max = 1000, message = "Le message ne doit pas dépasser 1000 caractères")
    private String message;

    // UTM (seront remplis automatiquement depuis l'URL)
    private String utmSource;
    private String utmMedium;
    private String utmCampaign;

    // Constructeurs
    public DonIntentionRequestDTO() {}

    // Getters et Setters
    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public DonIntention.MontantType getMontantType() { return montantType; }
    public void setMontantType(DonIntention.MontantType montantType) { this.montantType = montantType; }

    public DonIntention.ModePaiement getModePaiementSouhaite() { return modePaiementSouhaite; }
    public void setModePaiementSouhaite(DonIntention.ModePaiement modePaiementSouhaite) { this.modePaiementSouhaite = modePaiementSouhaite; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getUtmSource() { return utmSource; }
    public void setUtmSource(String utmSource) { this.utmSource = utmSource; }

    public String getUtmMedium() { return utmMedium; }
    public void setUtmMedium(String utmMedium) { this.utmMedium = utmMedium; }

    public String getUtmCampaign() { return utmCampaign; }
    public void setUtmCampaign(String utmCampaign) { this.utmCampaign = utmCampaign; }
}