package com.spring.spring.repository;

import com.spring.spring.entity.DonIntention;
import com.spring.spring.entity.DonIntention.StatutDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonIntentionRepository extends JpaRepository<DonIntention, Long> {

    // Recherche par statut
    Page<DonIntention> findByStatut(StatutDon statut, Pageable pageable);

    // Recherche par email
    List<DonIntention> findByEmailOrderByDateSoumissionDesc(String email);

    // Compter les soumissions récentes par email (pour anti-spam)
    long countByEmailAndDateSoumissionAfter(String email, LocalDateTime date);

    // Compter par IP (pour anti-spam)
    long countByIpAddressAndDateSoumissionAfter(String ipAddress, LocalDateTime date);

    // Statistiques : Nombre par statut
    @Query("SELECT d.statut, COUNT(d) FROM DonIntention d GROUP BY d.statut")
    List<Object[]> countByStatut();

    // Statistiques : Montant total des dons convertis
    @Query("SELECT SUM(d.montant) FROM DonIntention d WHERE d.statut = 'CONVERTI'")
    BigDecimal sumMontantConvertis();

    // Statistiques : Intentions par source UTM
    @Query("SELECT d.utmSource, COUNT(d) FROM DonIntention d WHERE d.utmSource IS NOT NULL GROUP BY d.utmSource")
    List<Object[]> countByUtmSource();

    // 👇 AJOUTER CETTE MÉTHODE POUR countByModePaiement
    @Query("SELECT d.modePaiementSouhaite, COUNT(d) FROM DonIntention d WHERE d.modePaiementSouhaite IS NOT NULL GROUP BY d.modePaiementSouhaite")
    List<Object[]> countByModePaiement();

    // Statistiques : Intentions par mois
    @Query("SELECT FUNCTION('DATE_TRUNC', 'month', d.dateSoumission), COUNT(d) FROM DonIntention d GROUP BY FUNCTION('DATE_TRUNC', 'month', d.dateSoumission) ORDER BY FUNCTION('DATE_TRUNC', 'month', d.dateSoumission) DESC")
    List<Object[]> countByMois();

    // Recherche avec filtres (pour l'admin)
    @Query("SELECT d FROM DonIntention d WHERE " +
            "(:nom IS NULL OR LOWER(d.nomComplet) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
            "(:email IS NULL OR LOWER(d.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:statut IS NULL OR d.statut = :statut) AND " +
            "(:dateDebut IS NULL OR d.dateSoumission >= :dateDebut) AND " +
            "(:dateFin IS NULL OR d.dateSoumission <= :dateFin)")
    Page<DonIntention> rechercherAvecFiltres(
            @Param("nom") String nom,
            @Param("email") String email,
            @Param("statut") StatutDon statut,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            Pageable pageable);
}