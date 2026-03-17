package com.spring.spring.service;

import com.spring.spring.dto.*;
import com.spring.spring.entity.DonIntention;
import com.spring.spring.entity.DonIntention.StatutDon;
import com.spring.spring.repository.DonIntentionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DonIntentionService {

    @Autowired
    private DonIntentionRepository donIntentionRepository;

    @Autowired
    private DonIntentionNotificationService notificationService;

    // Constantes pour anti-spam
    private static final int MAX_INTENTIONS_PAR_EMAIL_PAR_JOUR = 3;
    private static final int MAX_INTENTIONS_PAR_IP_PAR_JOUR = 5;

    /**
     * Créer une nouvelle intention de don (public)
     */
    @Transactional
    public DonIntentionResponseDTO createIntention(DonIntentionRequestDTO dto,
                                                   String ipAddress,
                                                   String userAgent,
                                                   String pays,
                                                   String ville) {

        // ✅ NETTOYER LE TÉLÉPHONE (enlever espaces, tirets, parenthèses)
        if (dto.getTelephone() != null) {
            String telephonePropre = dto.getTelephone()
                    .replaceAll("[\\s\\-()]", "")  // Enlève espaces, tirets, parenthèses
                    .trim();
            dto.setTelephone(telephonePropre);
        }

        // Vérification anti-spam
        verifierLimitesSoumission(dto.getEmail(), ipAddress);

        // Création de l'entité
        DonIntention intention = new DonIntention();
        intention.setNomComplet(dto.getNomComplet());
        intention.setEmail(dto.getEmail());
        intention.setTelephone(dto.getTelephone());
        intention.setMontant(dto.getMontant());
        intention.setMontantType(dto.getMontantType());
        intention.setModePaiementSouhaite(dto.getModePaiementSouhaite());
        intention.setMessage(dto.getMessage());

        // Données techniques
        intention.setUtmSource(dto.getUtmSource());
        intention.setUtmMedium(dto.getUtmMedium());
        intention.setUtmCampaign(dto.getUtmCampaign());
        intention.setIpAddress(ipAddress);
        intention.setUserAgent(userAgent);
        intention.setPays(pays);
        intention.setVille(ville);

        // Statut par défaut
        intention.setStatut(StatutDon.EN_ATTENTE);

        // Sauvegarde
        DonIntention savedIntention = donIntentionRepository.save(intention);

        // Envoi des notifications
        notificationService.envoyerConfirmationDonateur(savedIntention);
        notificationService.notifierEquipe(savedIntention);

        return new DonIntentionResponseDTO(savedIntention);
    }

    /**
     * Vérifier les limites de soumission (anti-spam)
     */
    private void verifierLimitesSoumission(String email, String ipAddress) {
        LocalDateTime ilYADate = LocalDateTime.now().minusDays(1);

        long soumissionsEmail = donIntentionRepository.countByEmailAndDateSoumissionAfter(email, ilYADate);
        if (soumissionsEmail >= MAX_INTENTIONS_PAR_EMAIL_PAR_JOUR) {
            throw new RuntimeException("Trop de demandes pour cet email. Réessayez plus tard.");
        }

        long soumissionsIP = donIntentionRepository.countByIpAddressAndDateSoumissionAfter(ipAddress, ilYADate);
        if (soumissionsIP >= MAX_INTENTIONS_PAR_IP_PAR_JOUR) {
            throw new RuntimeException("Trop de demandes depuis cette adresse IP. Réessayez plus tard.");
        }
    }

    /**
     * Récupérer toutes les intentions (admin) avec pagination
     */
    public Page<DonIntentionAdminDTO> getAllIntentions(Pageable pageable) {
        return donIntentionRepository.findAll(pageable)
                .map(DonIntentionAdminDTO::new);
    }

    /**
     * Récupérer une intention par son ID (admin)
     */
    public DonIntentionAdminDTO getIntentionById(Long id) {
        DonIntention intention = donIntentionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Intention de don non trouvée avec l'id: " + id));
        return new DonIntentionAdminDTO(intention);
    }

    /**
     * Récupérer une intention pour confirmation publique
     */
    public DonIntentionResponseDTO getIntentionByIdForConfirmation(Long id) {
        DonIntention intention = donIntentionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Intention de don non trouvée avec l'id: %d", id)
                ));
        return new DonIntentionResponseDTO(intention);
    }

    /**
     * Mettre à jour le statut d'une intention (admin)
     */
    @Transactional
    public DonIntentionAdminDTO updateStatut(Long id, StatutDon nouveauStatut, String notes) {
        DonIntention intention = donIntentionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Intention de don non trouvée"));

        // Mise à jour du statut avec les méthodes appropriées
        switch (nouveauStatut) {
            case CONTACTE:
                intention.marquerCommeContacte();
                break;
            case CONVERTI:
                intention.marquerCommeConverti();
                break;
            case PERDU:
                intention.marquerCommePerdu();
                break;
            case REPORTE:
                intention.marquerCommeReporte();
                break;
            default:
                intention.setStatut(nouveauStatut);
        }

        // Ajout des notes si fournies
        if (notes != null && !notes.isEmpty()) {
            String notesActuelles = intention.getNotesInternes();
            String nouvellesNotes = notesActuelles == null ?
                    notes :
                    notesActuelles + "\n" + LocalDateTime.now() + " : " + notes;
            intention.setNotesInternes(nouvellesNotes);
        }

        return new DonIntentionAdminDTO(donIntentionRepository.save(intention));
    }

    /**
     * Ajouter des notes internes (admin)
     */
    @Transactional
    public void addNotesInternes(Long id, String notes) {
        DonIntention intention = donIntentionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Intention de don non trouvée"));

        String notesActuelles = intention.getNotesInternes();
        String nouvellesNotes = notesActuelles == null ?
                notes :
                notesActuelles + "\n" + LocalDateTime.now() + " : " + notes;
        intention.setNotesInternes(nouvellesNotes);
        donIntentionRepository.save(intention);
    }
    /**
     * Supprimer une intention de don (admin uniquement)
     * Seul un administrateur peut supprimer des intentions
     */
    @Transactional
    public void deleteIntention(Long id) {
        // 1. Vérifier que l'utilisateur connecté est ADMIN
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new RuntimeException("Seul un administrateur peut supprimer des intentions de don");
        }

        // 2. Récupérer l'intention
        DonIntention intention = donIntentionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Intention de don non trouvée avec l'id: " + id));

        // 3. Optionnel : Empêcher la suppression des intentions déjà converties
        if (DonIntention.StatutDon.CONVERTI.equals(intention.getStatut())) {
            throw new RuntimeException("Impossible de supprimer une intention déjà convertie");
        }

        // 4. Supprimer l'intention
        donIntentionRepository.delete(intention);

        // 5. Logger l'action
        System.out.println("🗑️ Intention de don supprimée - ID: " + id + " par: " + authentication.getName());
    }
    /**
     * Obtenir les statistiques pour le dashboard
     */
    public DonIntentionStatistiquesDTO getStatistiques() {
        DonIntentionStatistiquesDTO stats = new DonIntentionStatistiquesDTO();

        // Total des intentions
        stats.setTotalIntentions(donIntentionRepository.count());

        // Détail par statut
        List<Object[]> countByStatut = donIntentionRepository.countByStatut();
        Map<String, Long> statsParStatut = new HashMap<>();
        for (Object[] row : countByStatut) {
            statsParStatut.put(((StatutDon) row[0]).name(), (Long) row[1]);
        }

        stats.setEnAttente(statsParStatut.getOrDefault("EN_ATTENTE", 0L));
        stats.setContactes(statsParStatut.getOrDefault("CONTACTE", 0L));
        stats.setConvertis(statsParStatut.getOrDefault("CONVERTI", 0L));
        stats.setPerdus(statsParStatut.getOrDefault("PERDU", 0L));

        // Montants des dons convertis
        BigDecimal montantTotal = donIntentionRepository.sumMontantConvertis();
        stats.setMontantTotalConverti(montantTotal != null ? montantTotal : BigDecimal.ZERO);

        // Montant moyen
        if (stats.getConvertis() > 0 && montantTotal != null) {
            BigDecimal moyen = montantTotal.divide(BigDecimal.valueOf(stats.getConvertis()), 2, RoundingMode.HALF_UP);
            stats.setMontantMoyenConverti(moyen);
        } else {
            stats.setMontantMoyenConverti(BigDecimal.ZERO);
        }

        // Taux de conversion
        if (stats.getTotalIntentions() > 0) {
            double taux = (stats.getConvertis() * 100.0) / stats.getTotalIntentions();
            stats.setTauxConversion(Math.round(taux * 100.0) / 100.0);
        }

        // Intentions par source UTM
        List<Object[]> parSource = donIntentionRepository.countByUtmSource();
        Map<String, Long> sourceMap = new HashMap<>();
        for (Object[] row : parSource) {
            sourceMap.put((String) row[0], (Long) row[1]);
        }
        stats.setIntentionsParSource(sourceMap);

        // Intentions par mode de paiement
        List<Object[]> parMode = donIntentionRepository.countByModePaiement();
        Map<String, Long> modeMap = new HashMap<>();
        for (Object[] row : parMode) {
            if (row[0] != null) {
                modeMap.put(((DonIntention.ModePaiement) row[0]).name(), (Long) row[1]);
            }
        }
        stats.setIntentionsParModePaiement(modeMap);

        // Intentions par mois
        List<Object[]> parMois = donIntentionRepository.countByMois();
        Map<String, Long> moisMap = new HashMap<>();
        for (Object[] row : parMois) {
            // Format: YYYY-MM
            String mois = row[0].toString().substring(0, 7);
            moisMap.put(mois, (Long) row[1]);
        }
        stats.setIntentionsParMois(moisMap);

        return stats;
    }

    /**
     * Recherche avancée avec filtres (admin)
     */
    public Page<DonIntentionAdminDTO> rechercherAvecFiltres(
            String nom, String email, StatutDon statut,
            LocalDateTime dateDebut, LocalDateTime dateFin,
            Pageable pageable) {

        return donIntentionRepository.rechercherAvecFiltres(nom, email, statut, dateDebut, dateFin, pageable)
                .map(DonIntentionAdminDTO::new);
    }
}
