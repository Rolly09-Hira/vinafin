package com.spring.spring.service;

import com.spring.spring.dto.PartenaireRequestDTO;
import com.spring.spring.entity.Partenaire;
import com.spring.spring.repository.PartenaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PartenaireService {

    @Autowired
    private PartenaireRepository partenaireRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public List<Partenaire> getAllPartenaires() {
        return partenaireRepository.findAll();
    }

    public Partenaire getPartenaireById(Long id) {
        return partenaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partenaire non trouvé avec l'ID: " + id));
    }

    @Transactional
    public Partenaire createPartenaire(PartenaireRequestDTO partenaireDTO) {
        Partenaire partenaire = new Partenaire();
        return saveOrUpdatePartenaire(partenaire, partenaireDTO);
    }

    @Transactional
    public Partenaire updatePartenaire(Long id, PartenaireRequestDTO partenaireDTO) {
        Partenaire partenaire = partenaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partenaire non trouvé avec l'ID: " + id));

        if (partenaireDTO.getLogoFile() != null && !partenaireDTO.getLogoFile().isEmpty()) {
            fileStorageService.deleteFile(partenaire.getLogoUrl());
        }

        return saveOrUpdatePartenaire(partenaire, partenaireDTO);
    }

    @Transactional
    public void deletePartenaire(Long id) {
        Partenaire partenaire = partenaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partenaire non trouvé avec l'ID: " + id));

        fileStorageService.deleteFile(partenaire.getLogoUrl());
        partenaireRepository.delete(partenaire);
    }

    private Partenaire saveOrUpdatePartenaire(Partenaire partenaire, PartenaireRequestDTO partenaireDTO) {
        String logoUrl = partenaire.getLogoUrl();
        if (partenaireDTO.getLogoFile() != null && !partenaireDTO.getLogoFile().isEmpty()) {
            // MODIFICATION ICI : ajout du 3ème paramètre "image"
            logoUrl = fileStorageService.storeFile(partenaireDTO.getLogoFile(), "partenaire", "image");
        }

        partenaire.setNom(partenaireDTO.getNom());
        partenaire.setType(Partenaire.TypePartenaire.valueOf(partenaireDTO.getType()));
        partenaire.setDescriptionFr(partenaireDTO.getDescriptionFr());
        partenaire.setDescriptionEn(partenaireDTO.getDescriptionEn());
        partenaire.setSiteWeb(partenaireDTO.getSiteWeb());
        partenaire.setEmail(partenaireDTO.getEmail());
        partenaire.setTelephone(partenaireDTO.getTelephone());
        partenaire.setAdresse(partenaireDTO.getAdresse());
        partenaire.setDateDebutPartenaire(partenaireDTO.getDateDebutPartenaire());
        partenaire.setActif(partenaireDTO.getActif() != null ? partenaireDTO.getActif() : true);
        partenaire.setLogoUrl(logoUrl);

        return partenaireRepository.save(partenaire);
    }
}