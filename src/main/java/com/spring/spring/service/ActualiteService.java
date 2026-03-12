package com.spring.spring.service;

import com.spring.spring.dto.ActualiteRequestDTO;
import com.spring.spring.entity.Actualite;
import com.spring.spring.repository.ActualiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActualiteService {

    @Autowired
    private ActualiteRepository actualiteRepository;

    @Autowired
    private CloudinaryStorageService cloudinaryStorageService; // CHANGÉ

    public List<Actualite> getAllActualites() {
        return actualiteRepository.findAll();
    }

    public Actualite getActualiteById(Long id) {
        return actualiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actualité non trouvée avec l'ID: " + id));
    }

    public List<Actualite> getActualitesImportantes() {
        return actualiteRepository.findAll()
                .stream()
                .filter(Actualite::getImportant)
                .toList();
    }

    public List<Actualite> getActualitesByType(String type) {
        return actualiteRepository.findAll()
                .stream()
                .filter(a -> a.getType().name().equals(type))
                .toList();
    }

    @Transactional
    public Actualite createActualite(ActualiteRequestDTO actualiteDTO) {
        Actualite actualite = new Actualite();
        return saveOrUpdateActualite(actualite, actualiteDTO);
    }

    @Transactional
    public Actualite updateActualite(Long id, ActualiteRequestDTO actualiteDTO) {
        Actualite actualite = actualiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actualité non trouvée avec l'ID: " + id));

        // Supprimer l'ancienne image si nouvelle fournie
        if (actualiteDTO.getImageFile() != null && !actualiteDTO.getImageFile().isEmpty()) {
            String oldPublicId = cloudinaryStorageService.extractPublicIdFromUrl(actualite.getImageUrl());
            if (oldPublicId != null) {
                cloudinaryStorageService.deleteFile(oldPublicId);
            }
        }

        return saveOrUpdateActualite(actualite, actualiteDTO);
    }

    @Transactional
    public void deleteActualite(Long id) {
        Actualite actualite = actualiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actualité non trouvée avec l'ID: " + id));

        // Supprimer l'image de Cloudinary
        String publicId = cloudinaryStorageService.extractPublicIdFromUrl(actualite.getImageUrl());
        if (publicId != null) {
            cloudinaryStorageService.deleteFile(publicId);
        }

        actualiteRepository.delete(actualite);
    }

    private Actualite saveOrUpdateActualite(Actualite actualite, ActualiteRequestDTO actualiteDTO) {
        String imageUrl = actualite.getImageUrl();
        
        // Upload nouvelle image si fournie
        if (actualiteDTO.getImageFile() != null && !actualiteDTO.getImageFile().isEmpty()) {
            // CHANGÉ: utilisation de cloudinaryStorageService.uploadImage()
            imageUrl = cloudinaryStorageService.uploadImage(actualiteDTO.getImageFile(), "actualite");
        }

        actualite.setTitreFr(actualiteDTO.getTitreFr());
        actualite.setTitreEn(actualiteDTO.getTitreEn());
        actualite.setContenuFr(actualiteDTO.getContenuFr());
        actualite.setContenuEn(actualiteDTO.getContenuEn());
        actualite.setType(Actualite.TypeActualite.valueOf(actualiteDTO.getType()));
        actualite.setDatePublication(actualiteDTO.getDatePublication());
        actualite.setDateEvenement(actualiteDTO.getDateEvenement());
        actualite.setLieu(actualiteDTO.getLieu());
        actualite.setImportant(actualiteDTO.getImportant() != null ? actualiteDTO.getImportant() : false);
        actualite.setImageUrl(imageUrl);

        return actualiteRepository.save(actualite);
    }
}
