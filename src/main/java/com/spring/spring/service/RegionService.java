package com.spring.spring.service;

import com.spring.spring.dto.RegionDTO;
import com.spring.spring.entity.Region;
import com.spring.spring.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<RegionDTO> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RegionDTO getRegionById(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Région non trouvée avec l'ID: " + id));
        return convertToDTO(region);
    }

    @Transactional
    public RegionDTO createRegion(RegionDTO regionDTO) {
        Region region = new Region();
        region.setNom(regionDTO.getNom());
        region.setDescription(regionDTO.getDescription());

        Region savedRegion = regionRepository.save(region);
        return convertToDTO(savedRegion);
    }

    @Transactional
    public RegionDTO updateRegion(Long id, RegionDTO regionDTO) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Région non trouvée avec l'ID: " + id));

        region.setNom(regionDTO.getNom());
        region.setDescription(regionDTO.getDescription());

        Region updatedRegion = regionRepository.save(region);
        return convertToDTO(updatedRegion);
    }

    @Transactional
    public void deleteRegion(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Région non trouvée avec l'ID: " + id));
        regionRepository.delete(region);
    }

    private RegionDTO convertToDTO(Region region) {
        return new RegionDTO(
                region.getId(),
                region.getNom(),
                region.getDescription(),
                region.getCreatedAt(),
                region.getUpdatedAt()
        );
    }
}