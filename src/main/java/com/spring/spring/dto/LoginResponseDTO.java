package com.spring.spring.dto;

public class LoginResponseDTO {
    private boolean success;
    private String message;
    private String nom;
    private String email;
    private String role;

    public LoginResponseDTO() {}

    public LoginResponseDTO(boolean success, String message, String nom, String email, String role) {
        this.success = success;
        this.message = message;
        this.nom = nom;
        this.email = email;
        this.role = role;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    public void setSuccess(boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
    public void setNom(String nom) { this.nom = nom; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
}