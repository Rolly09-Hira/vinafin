package com.spring.spring.service;

import com.spring.spring.entity.DonIntention;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
.util.Locale;

@Service
public class EmailBrevoService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.name}")
    private String appName;

    private String formatAriary(BigDecimal montant) {
        if (montant == null) return "Non spécifié";
        NumberFormat format = NumberFormat.getInstance(new Locale("fr", "MG"));
        return format.format(montant) + " Ar";
    }

    /**
     * Email de confirmation au donateur (format texte simple)
     */
    public void envoyerConfirmationDonateur(DonIntention intention) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(intention.getEmail());
            message.setSubject("🙏 Merci pour votre soutien à " + appName);

            String contenu = String.format(
                "Cher/Chère %s,\n\n" +
                "Nous avons bien reçu votre souhait de soutenir nos actions à %s.\n\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "📋 RÉCAPITULATIF DE VOTRE DEMANDE\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                "➤ Référence : DON-%d\n" +
                "➤ Montant : %s\n" +
                "➤ Mode de paiement : %s\n" +
                "➤ Date : %s\n\n" +
                "➤ Message : %s\n\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                "Un membre de notre équipe vous contactera dans les 24 à 48 heures " +
                "pour finaliser votre don.\n\n" +
                "Ensemble, construisons un avenir meilleur pour les communautés rurales.\n\n" +
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

            message.setText(contenu);
            mailSender.send(message);
            System.out.println("✅ Email confirmation envoyé à " + intention.getEmail());

        } catch (Exception e) {
            System.err.println("❌ Erreur envoi confirmation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Notification à l'équipe (format HTML pour meilleure lisibilité)
     */
    public void notifierEquipe(DonIntention intention, String teamEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(teamEmail);
            helper.setSubject("🔔 NOUVELLE INTENTION DE DON - " + intention.getNomComplet());

            String contenuHtml = String.format(
                "<!DOCTYPE html>" +
                "<html>" +
                "<head><meta charset='UTF-8'><style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; text-align: center; }" +
                ".content { padding: 20px; }" +
                ".section { margin: 20px 0; padding: 15px; background: #f9f9f9; border-left: 4px solid #667eea; }" +
                ".label { font-weight: bold; color: #555; }" +
                ".value { color: #333; margin-left: 10px; }" +
                ".footer { text-align: center; padding: 20px; color: #777; font-size: 12px; }" +
                "</style></head>" +
                "<body>" +
                "<div class='header'><h2>🔔 Nouvelle intention de don</h2></div>" +
                "<div class='content'>" +
                
                "<div class='section'>" +
                "<h3>👤 DONATEUR</h3>" +
                "<p><span class='label'>Nom :</span><span class='value'>%s</span></p>" +
                "<p><span class='label'>Email :</span><span class='value'>%s</span></p>" +
                "<p><span class='label'>Téléphone :</span><span class='value'>%s</span></p>" +
                "<p><span class='label'>Localisation :</span><span class='value'>%s, %s</span></p>" +
                "</div>" +
                
                "<div class='section'>" +
                "<h3>💰 DON</h3>" +
                "<p><span class='label'>Montant :</span><span class='value'>%s</span></p>" +
                "<p><span class='label'>Mode souhaité :</span><span class='value'>%s</span></p>" +
                "<p><span class='label'>Type :</span><span class='value'>%s</span></p>" +
                "</div>" +
                
                "<div class='section'>" +
                "<h3>💬 MESSAGE</h3>" +
                "<p>%s</p>" +
                "</div>" +
                
                "<div class='section'>" +
                "<h3>📊 INFORMATIONS TECHNIQUES</h3>" +
                "<p><span class='label'>IP :</span><span class='value'>%s</span></p>" +
                "<p><span class='label'>Date :</span><span class='value'>%s</span></p>" +
                "</div>" +
                
                "</div>" +
                "<div class='footer'>Connectez-vous au dashboard pour traiter cette demande.</div>" +
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

            helper.setText(contenuHtml, true); // true = contenu HTML
            mailSender.send(message);
            System.out.println("✅ Notification équipe envoyée à " + teamEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Erreur envoi notification équipe: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
