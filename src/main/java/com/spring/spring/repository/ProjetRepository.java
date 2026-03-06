package com.spring.spring.repository;

import com.spring.spring.entity.Projet;
import com.spring.spring.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
    // 🌟 NOUVELLES MÉTHODES DE RECHERCHE
    List<Projet> findByRegion(Region region);
    List<Projet> findByRegionId(Long regionId);
    List<Projet> findByBeneficiairesGreaterThan(Integer seuil);
    List<Projet> findByRegionIdAndStatut(Long regionId, Projet.StatutProjet statut);
}