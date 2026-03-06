package com.spring.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class RegionDTO {

    // Pour les réponses (peut être null en request)
    private Long id;

    @NotBlank(message = "Le nom est requis")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nom;

    private String description;

    // Pour les réponses seulement (ignorés en request)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructeurs
    public RegionDTO() {}

    public RegionDTO(Long id, String nom, String description,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}