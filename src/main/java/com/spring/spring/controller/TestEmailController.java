package com.spring.spring.controller;

import com.spring.spring.entity.DonIntention;
import com.spring.spring.service.EmailBrevoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
public class TestEmailController {

    @Autowired
    private EmailBrevoService emailBrevoService;

    @Value("${app.email.team}")
    private String teamEmail;

    @GetMapping("/brevo")
    public String testBrevo() {
        try {
            // Créer une intention de test
            DonIntention test = new DonIntention();
            test.setId(999L);
            test.setNomComplet("Testeur VINA");
            test.setEmail(teamEmail);  // S'envoie à vous-même
            test.setTelephone("+261341234567");
            test.setMontant(new BigDecimal("50000"));
            test.setMontantType(DonIntention.MontantType.FIXE);
            test.setModePaiementSouhaite(DonIntention.ModePaiement.ORANGE_MONEY);
            test.setMessage("Test Brevo SMTP - Ceci est un message de test");
            test.setDateSoumission(LocalDateTime.now());
            test.setPays("Madagascar");
            test.setVille("Antananarivo");
            test.setIpAddress("127.0.0.1");
            test.setUserAgent("Test Agent");

            // Envoyer les emails
            emailBrevoService.envoyerConfirmationDonateur(test);
            emailBrevoService.notifierEquipe(test, teamEmail);
            
            return "✅ Test Brevo réussi ! Vérifiez votre boîte email : " + teamEmail;
        } catch (Exception e) {
            return "❌ Erreur: " + e.getMessage() + "\n" + 
                   "Vérifiez que votre clé SMTP est correcte dans les variables d'environnement.";
        }
    }
}
