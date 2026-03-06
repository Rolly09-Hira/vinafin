package com.spring.spring.repository;

import com.spring.spring.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {

    Optional<Personnel> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Personnel> findAllByOrderByOrdreAffichageAscNomAsc();

    List<Personnel> findByDepartementOrderByOrdreAffichageAsc(String departement);

    @Query("SELECT p FROM Personnel p ORDER BY p.ordreAffichage ASC NULLS LAST, p.nom ASC")
    List<Personnel> findAllOrdered();

    @Query("SELECT COUNT(p) FROM Personnel p")
    long countTotalPersonnel();
}