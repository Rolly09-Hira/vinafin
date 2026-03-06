package com.spring.spring.repository;

import com.spring.spring.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    List<Region> findByNomContainingIgnoreCase(String nom);
}