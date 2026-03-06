package com.spring.spring.repository;

import com.spring.spring.entity.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {

    Optional<ContactInfo> findByTitre(String titre);

    List<ContactInfo> findByTypeContact(String typeContact);

    List<ContactInfo> findByActifTrueOrderByOrdreAffichageAsc();

    boolean existsByTitre(String titre);
}