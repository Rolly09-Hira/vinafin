package com.spring.spring.service;

import com.spring.spring.dto.ProjetRequestDTO;
import com.spring.spring.entity.Projet;
import com.spring.spring.entity.Region;
import com.spring.spring.repository.ProjetRepository;
import com.spring.spring.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private RegionRepository regionRepository;  // 🌟 NOUVEAU

    @Autowired
    private FileStorageService fileStorageService;

    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }

    public Projet getProjetById(Long id) {
        return projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + id));
    }

    @Transactional
    public Projet createProjet(ProjetRequestDTO projetDTO) {
        Projet projet = new Projet();
        return saveOrUpdateProjet(projet, projetDTO);
    }

    @Transactional
    public Projet updateProjet(Long id, ProjetRequestDTO projetDTO) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + id));

        if (projetDTO.getImageFile() != null && !projetDTO.getImageFile().isEmpty()) {
            fileStorageService.deleteFile(projet.getImageUrl());
        }

        return saveOrUpdateProjet(projet, projetDTO);
    }

    @Transactional
    public void deleteProjet(Long id) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + id));

        fileStorageService.deleteFile(projet.getImageUrl());
        projetRepository.delete(projet);
    }

    private Projet saveOrUpdateProjet(Projet projet, ProjetRequestDTO projetDTO) {
        String imageUrl = projet.getImageUrl();
        if (projetDTO.getImageFile() != null && !projetDTO.getImageFile().isEmpty()) {
            imageUrl = fileStorageService.storeFile(projetDTO.getImageFile(), "projet", "image");
        }

        projet.setTitreFr(projetDTO.getTitreFr());
        projet.setTitreEn(projetDTO.getTitreEn());
        projet.setDescriptionFr(projetDTO.getDescriptionFr());
        projet.setDescriptionEn(projetDTO.getDescriptionEn());
        projet.setObjectifFr(projetDTO.getObjectifFr());
        projet.setObjectifEn(projetDTO.getObjectifEn());
        projet.setDateDebut(projetDTO.getDateDebut());
        projet.setDateFin(projetDTO.getDateFin());
        projet.setStatut(Projet.StatutProjet.valueOf(projetDTO.getStatut()));
        projet.setImageUrl(imageUrl);
        projet.setDomaineFr(projetDTO.getDomaineFr());
        projet.setDomaineEn(projetDTO.getDomaineEn());

        if (projetDTO.getRegionId() != null) {
            Region region = regionRepository.findById(projetDTO.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Région non trouvée avec l'ID: " + projetDTO.getRegionId()));
            projet.setRegion(region);
        }

        projet.setBeneficiaires(projetDTO.getBeneficiaires());

        return projetRepository.save(projet);
    }
}