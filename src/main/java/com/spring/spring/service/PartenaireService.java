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
    private CloudinaryStorageService cloudinaryStorageService; // CHANGÉ

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

        // Supprimer l'ancien logo si nouveau fourni
        if (partenaireDTO.getLogoFile() != null && !partenaireDTO.getLogoFile().isEmpty()) {
            String oldPublicId = cloudinaryStorageService.extractPublicIdFromUrl(partenaire.getLogoUrl());
            if (oldPublicId != null) {
                cloudinaryStorageService.deleteFile(oldPublicId);
            }
        }

        return saveOrUpdatePartenaire(partenaire, partenaireDTO);
    }

    @Transactional
    public void deletePartenaire(Long id) {
        Partenaire partenaire = partenaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partenaire non trouvé avec l'ID: " + id));

        // Supprimer le logo de Cloudinary
        String publicId = cloudinaryStorageService.extractPublicIdFromUrl(partenaire.getLogoUrl());
        if (publicId != null) {
            cloudinaryStorageService.deleteFile(publicId);
        }

        partenaireRepository.delete(partenaire);
    }

    private Partenaire saveOrUpdatePartenaire(Partenaire partenaire, PartenaireRequestDTO partenaireDTO) {
        String logoUrl = partenaire.getLogoUrl();
        
        // Upload nouveau logo si fourni
        if (partenaireDTO.getLogoFile() != null && !partenaireDTO.getLogoFile().isEmpty()) {
            // CHANGÉ: utilisation de uploadImage()
            logoUrl = cloudinaryStorageService.uploadImage(partenaireDTO.getLogoFile(), "partenaire");
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
