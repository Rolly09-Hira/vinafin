package com.spring.spring.repository;

import com.spring.spring.entity.Temoignage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemoignageRepository extends JpaRepository<Temoignage, Long> {
}