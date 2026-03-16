package com.spring.spring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.spring.entity.DonIntention;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

@Service
public class EmailBrevoService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.api.url:https://api.brevo.com/v3}")
    private String apiUrl;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.name:VINA Association}")
    private String appName;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String formatAriary(BigDecimal montant) {
        if (montant == null) return "Non spécifié";
        NumberFormat format = NumberFormat.getInstance(new Locale("fr", "MG"));
        return format.format(montant) + " Ar";
    }

    /**
     * Envoie un email via l'API Brevo (pas SMTP !)
     */
    private boolean sendEmailViaBrevo(String to, String subject, String htmlContent, String textContent) {
        try {
            // Construction de la requête pour Brevo API
            Map<String, Object> requestBody = new HashMap<>();
            
            // Expéditeur
            Map<String, String> sender = new HashMap<>();
            sender.put("name", appName);
            sender.put("email", fromEmail);
            requestBody.put("sender", sender);
            
            // Destinataire
            List<Map<String, String>> toList = new ArrayList<>();
            Map<String, String> recipient = new HashMap<>();
            recipient.put("email", to);
            toList.add(recipient);
            requestBody.put("to", toList);
            
            // Contenu
            requestBody.put("subject", subject);
            requestBody.put("htmlContent", htmlContent);
            requestBody.put("textContent", textContent);

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Appel API Brevo
            String url = apiUrl + "/smtp/email";
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                entity, 
                String.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED || 
                response.getStatusCode() == HttpStatus.OK || 
                response.getStatusCode() == HttpStatus.ACCEPTED) {
                System.out.println("✅ Email Brevo envoyé à " + to + " - Sujet: " + subject);
                System.out.println("   Réponse: " + response.getStatusCode());
                return true;
            } else {
                System.err.println("❌ Erreur Brevo: " + response.getStatusCode());
                System.err.println("   Corps: " + response.getBody());
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Exception lors de l'envoi Brevo: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Email de confirmation au donateur
     */
    public void envoyerConfirmationDonateur(DonIntention intention) {
        String sujet = "🙏 Merci pour votre soutien à " + appName;
        
        String contenuTexte = String.format(
            "Cher/Chère %s,\n\n" +
            "Nous avons bien reçu votre souhait de soutenir nos actions à %s.\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "RÉCAPITULATIF DE VOTRE DEMANDE\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "➤ Référence : DON-%d\n" +
            "➤ Montant : %s\n" +
            "➤ Mode de paiement : %s\n" +
            "➤ Date : %s\n\n" +
            "➤ Message : %s\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "Un membre de notre équipe vous contactera dans les 24 à 48 heures.\n\n" +
            "L'équipe %s\n" +
            "Email : %s",
            intention.getNomComplet(),
            appName,
            intention.getId(),
            formatAriary(intention.getMontant()),
            intention.getModePaiementSouhaite() != null ? 
                intention.getModePaiementSouhaite().toString().replace("_", " ") : "Non spécifié",
            intention.getDateSoumission().toLocalDate().toString(),
            intention.getMessage() != null ? intention.getMessage() : "Pas de message",
            appName,
            fromEmail
        );

        String contenuHtml = String.format(
            "<!DOCTYPE html>" +
            "<html><head><meta charset='UTF-8'><style>" +
            "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
            ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
            ".header { background: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }" +
            ".content { padding: 20px; background: #f9f9f9; }" +
            ".footer { text-align: center; padding: 20px; color: #777; font-size: 12px; }" +
            "</style></head>" +
            "<body><div class='container'>" +
            "<div class='header'><h2>🙏 Merci pour votre soutien</h2></div>" +
            "<div class='content'>" +
            "<p>Cher/Chère %s,</p>" +
            "<p>Nous avons bien reçu votre souhait de soutenir nos actions à %s.</p>" +
            "<h3>Récapitulatif de votre demande :</h3>" +
            "<ul>" +
            "<li><strong>Référence :</strong> DON-%d</li>" +
            "<li><strong>Montant :</strong> %s</li>" +
            "<li><strong>Mode de paiement :</strong> %s</li>" +
            "<li><strong>Date :</strong> %s</li>" +
            "</ul>" +
            "<p><strong>Message :</strong> %s</p>" +
            "<p>Un membre de notre équipe vous contactera dans les 24 à 48 heures.</p>" +
            "</div>" +
            "<div class='footer'>L'équipe %s</div>" +
            "</div></body></html>",
            intention.getNomComplet(),
            appName,
            intention.getId(),
            formatAriary(intention.getMontant()),
            intention.getModePaiementSouhaite() != null ? 
                intention.getModePaiementSouhaite().toString().replace("_", " ") : "Non spécifié",
            intention.getDateSoumission().toLocalDate().toString(),
            intention.getMessage() != null ? intention.getMessage() : "Pas de message",
            appName
        );

        sendEmailViaBrevo(intention.getEmail(), sujet, contenuHtml, contenuTexte);
    }

    /**
     * Notification à l'équipe
     */
    public void notifierEquipe(DonIntention intention, String teamEmail) {
        String sujet = "🔔 NOUVELLE INTENTION DE DON - " + intention.getNomComplet();
        
        String contenuTexte = String.format(
            "NOUVELLE INTENTION DE DON\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "👤 DONATEUR\n" +
            "Nom : %s\n" +
            "Email : %s\n" +
            "Téléphone : %s\n" +
            "Localisation : %s, %s\n\n" +
            "💰 DON\n" +
            "Montant : %s\n" +
            "Mode souhaité : %s\n" +
            "Type : %s\n\n" +
            "💬 MESSAGE\n" +
            "%s\n\n" +
            "📊 TECHNIQUE\n" +
            "IP : %s\n" +
            "Date : %s\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "Connectez-vous au dashboard.",
            intention.getNomComplet(),
            intention.getEmail(),
            intention.getTelephone(),
            intention.getPays() != null ? intention.getPays() : "Inconnu",
            intention.getVille() != null ? intention.getVille() : "Inconnue",
            formatAriary(intention.getMontant()),
            intention.getModePaiementSouhaite() != null ? 
                intention.getModePaiementSouhaite().toString().replace("_", " ") : "Non spécifié",
            intention.getMontantType() != null ? intention.getMontantType() : "Non spécifié",
            intention.getMessage() != null ? intention.getMessage() : "Pas de message",
            intention.getIpAddress() != null ? intention.getIpAddress() : "Inconnue",
            intention.getDateSoumission().toString()
        );

        String contenuHtml = String.format(
            "<!DOCTYPE html><html><head><style>" +
            "body { font-family: Arial; line-height: 1.6; }" +
            ".header { background: #f44336; color: white; padding: 10px; }" +
            ".section { margin: 20px 0; padding: 15px; background: #f5f5f5; }" +
            "</style></head><body>" +
            "<div class='header'><h2>🔔 NOUVELLE INTENTION DE DON</h2></div>" +
            "<div class='section'><h3>👤 DONATEUR</h3>" +
            "<p><strong>Nom :</strong> %s<br><strong>Email :</strong> %s<br>" +
            "<strong>Téléphone :</strong> %s<br><strong>Localisation :</strong> %s, %s</p></div>" +
            "<div class='section'><h3>💰 DON</h3>" +
            "<p><strong>Montant :</strong> %s<br><strong>Mode :</strong> %s<br>" +
            "<strong>Type :</strong> %s</p></div>" +
            "<div class='section'><h3>💬 MESSAGE</h3><p>%s</p></div>" +
            "<div class='section'><h3>📊 TECHNIQUE</h3>" +
            "<p><strong>IP :</strong> %s<br><strong>Date :</strong> %s</p></div>" +
            "<p style='text-align: center; color: #777;'>Connectez-vous au dashboard</p>" +
            "</body></html>",
            intention.getNomComplet(),
            intention.getEmail(),
            intention.getTelephone(),
            intention.getPays() != null ? intention.getPays() : "Inconnu",
            intention.getVille() != null ? intention.getVille() : "Inconnue",
            formatAriary(intention.getMontant()),
            intention.getModePaiementSouhaite() != null ? 
                intention.getModePaiementSouhaite().toString().replace("_", " ") : "Non spécifié",
            intention.getMontantType() != null ? intention.getMontantType() : "Non spécifié",
            intention.getMessage() != null ? intention.getMessage() : "Pas de message",
            intention.getIpAddress() != null ? intention.getIpAddress() : "Inconnue",
            intention.getDateSoumission().toString()
        );

        sendEmailViaBrevo(teamEmail, sujet, contenuHtml, contenuTexte);
    }
}
