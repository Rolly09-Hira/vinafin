package com.spring.spring.dto;

public class LoginRequestDTO {
    private String email;
    private String motDePasse;

    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }

    public void setEmail(String email) { this.email = email; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
}