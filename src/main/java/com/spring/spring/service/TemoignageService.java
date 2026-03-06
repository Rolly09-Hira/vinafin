package com.spring.spring.service;

import com.spring.spring.dto.TemoignageRequestDTO;
import com.spring.spring.entity.Temoignage;
import com.spring.spring.repository.TemoignageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TemoignageService {

    @Autowired
    private TemoignageRepository temoignageRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public List<Temoignage> getAllTemoignages() {
        return temoignageRepository.findAll();
    }

    public List<Temoignage> getTemoignagesActifs() {
        return temoignageRepository.findAll()
                .stream()
                .filter(Temoignage::getActif)
                .sorted((t1, t2) -> {
                    if (t1.getOrdreAffichage() != null && t2.getOrdreAffichage() != null) {
                        return t1.getOrdreAffichage().compareTo(t2.getOrdreAffichage());
                    }
                    return t2.getDatePublication().compareTo(t1.getDatePublication());
                })
                .toList();
    }

    public List<Temoignage> getTemoignagesByType(String type) {
        return temoignageRepository.findAll()
                .stream()
                .filter(t -> t.getTypeTemoignage().name().equals(type))
                .filter(Temoignage::getActif)
                .toList();
    }

    public Temoignage getTemoignageById(Long id) {
        return temoignageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Témoignage non trouvé avec l'ID: " + id));
    }

    @Transactional
    public Temoignage createTemoignage(TemoignageRequestDTO temoignageDTO) {
        Temoignage temoignage = new Temoignage();
        return saveOrUpdateTemoignage(temoignage, temoignageDTO);
    }

    @Transactional
    public Temoignage updateTemoignage(Long id, TemoignageRequestDTO temoignageDTO) {
        Temoignage temoignage = temoignageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Témoignage non trouvé avec l'ID: " + id));

        return saveOrUpdateTemoignage(temoignage, temoignageDTO);
    }

    @Transactional
    public void deleteTemoignage(Long id) {
        Temoignage temoignage = temoignageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Témoignage non trouvé avec l'ID: " + id));

        fileStorageService.deleteFile(temoignage.getPhotoUrl());
        fileStorageService.deleteFile(temoignage.getVideoUrl());
        temoignageRepository.delete(temoignage);
    }

    private Temoignage saveOrUpdateTemoignage(Temoignage temoignage, TemoignageRequestDTO temoignageDTO) {
        // Gérer l'upload de la photo
        String photoUrl = temoignage.getPhotoUrl();
        if (temoignageDTO.getPhotoFile() != null && !temoignageDTO.getPhotoFile().isEmpty()) {
            fileStorageService.deleteFile(photoUrl);
            photoUrl = fileStorageService.storeFile(temoignageDTO.getPhotoFile(), "temoignage", "image");
        }

        // Gérer l'upload de la vidéo
        String videoUrl = temoignage.getVideoUrl();
        if (temoignageDTO.getVideoFile() != null && !temoignageDTO.getVideoFile().isEmpty()) {
            fileStorageService.deleteFile(videoUrl);
            videoUrl = fileStorageService.storeFile(temoignageDTO.getVideoFile(), "temoignage", "video");
        }

        // Déterminer automatiquement le type de témoignage
        Temoignage.TypeTemoignage typeTemoignage = Temoignage.TypeTemoignage.PHOTO;

        boolean hasPhoto = (photoUrl != null && !photoUrl.isEmpty());
        boolean hasVideo = (videoUrl != null && !videoUrl.isEmpty());

        if (hasPhoto && hasVideo) {
            typeTemoignage = Temoignage.TypeTemoignage.PHOTO_VIDEO;
        } else if (hasVideo) {
            typeTemoignage = Temoignage.TypeTemoignage.VIDEO;
        } else if (hasPhoto) {
            typeTemoignage = Temoignage.TypeTemoignage.PHOTO;
        }

        if (temoignageDTO.getTypeTemoignage() != null && !temoignageDTO.getTypeTemoignage().isEmpty()) {
            typeTemoignage = Temoignage.TypeTemoignage.valueOf(temoignageDTO.getTypeTemoignage());
        }

        temoignage.setAuteurFr(temoignageDTO.getAuteurFr());
        temoignage.setAuteurEn(temoignageDTO.getAuteurEn());
        temoignage.setFonctionFr(temoignageDTO.getFonctionFr());
        temoignage.setFonctionEn(temoignageDTO.getFonctionEn());
        temoignage.setContenuFr(temoignageDTO.getContenuFr());
        temoignage.setContenuEn(temoignageDTO.getContenuEn());
        temoignage.setDatePublication(temoignageDTO.getDatePublication());
        temoignage.setActif(temoignageDTO.getActif() != null ? temoignageDTO.getActif() : true);
        temoignage.setOrdreAffichage(temoignageDTO.getOrdreAffichage());
        temoignage.setPhotoUrl(photoUrl);
        temoignage.setVideoUrl(videoUrl);
        temoignage.setTypeTemoignage(typeTemoignage);

        return temoignageRepository.save(temoignage);
    }
}