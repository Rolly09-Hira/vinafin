package com.spring.spring.service;

import com.spring.spring.entity.DonIntention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal; // 👈 AJOUTER CET IMPORT
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class DonIntentionNotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.team}")
    private String teamEmail;

    private String formatAriary(BigDecimal montant) {
        if (montant == null) return "Non spécifié";
        NumberFormat format = NumberFormat.getInstance(new Locale("fr", "MG"));
        return format.format(montant) + " Ar";
    }

    /**
     * Email de confirmation au donateur
     */
    public void envoyerConfirmationDonateur(DonIntention intention) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(intention.getEmail());
            message.setSubject("Merci pour votre intérêt à soutenir VINA");

            String contenu = String.format(
                    "Cher/Chère %s,\n\n" +
                            "Nous avons bien reçu votre souhait de soutenir nos actions à VINA.\n\n" +
                            "Récapitulatif de votre demande :\n" +
                            "➤ Référence : DON-%d\n" +
                            "➤ Montant : %s\n" +
                            "➤ Mode de paiement souhaité : %s\n" +
                            "➤ Date : %s\n\n" +
                            "Un membre de notre équipe vous contactera dans les 24 à 48 heures pour finaliser votre don.\n\n" +
                            "Ensemble, construisons un avenir meilleur pour les communautés rurales.\n\n" +
                            "L'équipe VINA\n" +
                            "Tél : +261 XX XXX XX XX\n" +
                            "Email : contact@vina.org",
                    intention.getNomComplet(),
                    intention.getId(),
                    formatAriary(intention.getMontant()),
                    intention.getModePaiementSouhaite() != null ? intention.getModePaiementSouhaite().toString() : "Non spécifié",
                    intention.getDateSoumission().toLocalDate().toString()
            );

            message.setText(contenu);
            mailSender.send(message);

            System.out.println("✅ Email de confirmation envoyé à " + intention.getEmail());

        } catch (Exception e) {
            System.err.println("❌ Erreur envoi email au donateur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Notification à l'équipe
     */
    public void notifierEquipe(DonIntention intention) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(teamEmail);
            message.setSubject("🔔 NOUVELLE INTENTION DE DON - Action requise");

            String contenu = String.format(
                    "Nouvelle intention de don reçue !\n\n" +
                            "━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                            "👤 DONATEUR\n" +
                            "Nom : %s\n" +
                            "Email : %s\n" +
                            "Téléphone : %s\n\n" +
                            "💰 DON\n" +
                            "Montant : %s\n" +
                            "Mode souhaité : %s\n" +
                            "Type : %s\n\n" +
                            "💬 MESSAGE\n" +
                            "%s\n\n" +
                            "📊 TRACKING\n" +
                            "Source : %s %s %s\n" +
                            "Pays : %s\n" +
                            "Ville : %s\n\n" +
                            "━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                            "🔗 Lien direct : http://localhost:5173/admin/dons/%d\n" +
                            "📅 Soumission : %s\n\n" +
                            "Connectez-vous au dashboard pour traiter cette demande.",
                    intention.getNomComplet(),
                    intention.getEmail(),
                    intention.getTelephone(),
                    formatAriary(intention.getMontant()),
                    intention.getModePaiementSouhaite() != null ? intention.getModePaiementSouhaite().toString() : "Non spécifié",
                    intention.getMontantType() != null ? intention.getMontantType().toString() : "Non spécifié",
                    intention.getMessage() != null ? intention.getMessage() : "Pas de message",
                    intention.getUtmSource() != null ? intention.getUtmSource() : "-",
                    intention.getUtmMedium() != null ? intention.getUtmMedium() : "-",
                    intention.getUtmCampaign() != null ? intention.getUtmCampaign() : "-",
                    intention.getPays() != null ? intention.getPays() : "Inconnu",
                    intention.getVille() != null ? intention.getVille() : "Inconnue",
                    intention.getId(),
                    intention.getDateSoumission().toString()
            );

            message.setText(contenu);
            mailSender.send(message);

            System.out.println("✅ Notification envoyée à l'équipe (" + teamEmail + ")");

        } catch (Exception e) {
            System.err.println("❌ Erreur envoi notification équipe: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Rappel pour intention non traitée après 48h
     */
    public void envoyerRappelEquipe(DonIntention intention) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(teamEmail);
            message.setSubject("⏰ RAPPEL : Intention de don en attente depuis 48h");

            String contenu = String.format(
                    "L'intention de don suivante est en attente depuis plus de 48h :\n\n" +
                            "ID: %d\n" +
                            "Donateur: %s\n" +
                            "Email: %s\n" +
                            "Téléphone: %s\n" +
                            "Montant: %s\n" +
                            "Date soumission: %s\n\n" +
                            "Veuillez traiter cette demande rapidement.",
                    intention.getId(),
                    intention.getNomComplet(),
                    intention.getEmail(),
                    intention.getTelephone(),
                    formatAriary(intention.getMontant()),
                    intention.getDateSoumission().toString()
            );

            message.setText(contenu);
            mailSender.send(message);

        } catch (Exception e) {
            System.err.println("❌ Erreur envoi rappel: " + e.getMessage());
        }
    }
}