package com.spring.spring.dto;

import java.math.BigDecimal;
import java.util.Map;

public class DonIntentionStatistiquesDTO {

    private long totalIntentions;
    private long enAttente;
    private long contactes;
    private long convertis;
    private long perdus;
    private BigDecimal montantTotalConverti;
    private BigDecimal montantMoyenConverti;
    private double tauxConversion;
    private Map<String, Long> intentionsParSource;
    private Map<String, Long> intentionsParModePaiement;
    private Map<String, Long> intentionsParMois;

    // Constructeurs
    public DonIntentionStatistiquesDTO() {}

    // Getters et Setters
    public long getTotalIntentions() { return totalIntentions; }
    public void setTotalIntentions(long totalIntentions) { this.totalIntentions = totalIntentions; }

    public long getEnAttente() { return enAttente; }
    public void setEnAttente(long enAttente) { this.enAttente = enAttente; }

    public long getContactes() { return contactes; }
    public void setContactes(long contactes) { this.contactes = contactes; }

    public long getConvertis() { return convertis; }
    public void setConvertis(long convertis) { this.convertis = convertis; }

    public long getPerdus() { return perdus; }
    public void setPerdus(long perdus) { this.perdus = perdus; }

    public BigDecimal getMontantTotalConverti() { return montantTotalConverti; }
    public void setMontantTotalConverti(BigDecimal montantTotalConverti) { this.montantTotalConverti = montantTotalConverti; }

    public BigDecimal getMontantMoyenConverti() { return montantMoyenConverti; }
    public void setMontantMoyenConverti(BigDecimal montantMoyenConverti) { this.montantMoyenConverti = montantMoyenConverti; }

    public double getTauxConversion() { return tauxConversion; }
    public void setTauxConversion(double tauxConversion) { this.tauxConversion = tauxConversion; }

    public Map<String, Long> getIntentionsParSource() { return intentionsParSource; }
    public void setIntentionsParSource(Map<String, Long> intentionsParSource) { this.intentionsParSource = intentionsParSource; }

    public Map<String, Long> getIntentionsParModePaiement() { return intentionsParModePaiement; }
    public void setIntentionsParModePaiement(Map<String, Long> intentionsParModePaiement) { this.intentionsParModePaiement = intentionsParModePaiement; }

    public Map<String, Long> getIntentionsParMois() { return intentionsParMois; }
    public void setIntentionsParMois(Map<String, Long> intentionsParMois) { this.intentionsParMois = intentionsParMois; }
}