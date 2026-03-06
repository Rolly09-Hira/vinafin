package com.spring.spring.dto;

import com.spring.spring.entity.DonIntention;
import java.time.LocalDateTime;

public class DonIntentionResponseDTO {

    private Long id;
    private String nomComplet;
    private String email;
    private String telephone;
    private String message;
    private LocalDateTime dateSoumission;
    private String reference; // Rérence pour suivi

    public DonIntentionResponseDTO() {}

    public DonIntentionResponseDTO(DonIntention intention) {
        this.id = intention.getId();
        this.nomComplet = intention.getNomComplet();
        this.email = intention.getEmail();
        this.telephone = intention.getTelephone();
        this.message = intention.getMessage();
        this.dateSoumission = intention.getDateSoumission();
        this.reference = "DON-" + intention.getId() + "-" + intention.getDateSoumission().getYear();
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(LocalDateTime dateSoumission) { this.dateSoumission = dateSoumission; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
}