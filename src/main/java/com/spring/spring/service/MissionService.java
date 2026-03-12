package com.spring.spring.service;

import com.spring.spring.dto.MissionRequestDTO;
import com.spring.spring.entity.Mission;
import com.spring.spring.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private CloudinaryStorageService cloudinaryStorageService; // CHANGÉ

    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    public List<Mission> getMissionsActives() {
        return missionRepository.findByActifTrueOrderByOrdreAffichageAsc();
    }

    public Mission getMissionById(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée avec l'ID: " + id));
    }

    @Transactional
    public Mission createMission(MissionRequestDTO missionDTO) {
        Mission mission = new Mission();
        return saveOrUpdateMission(mission, missionDTO);
    }

    @Transactional
    public Mission updateMission(Long id, MissionRequestDTO missionDTO) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée avec l'ID: " + id));

        return saveOrUpdateMission(mission, missionDTO);
    }

    @Transactional
    public void deleteMission(Long id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée avec l'ID: " + id));

        // Supprimer les fichiers de Cloudinary
        String iconPublicId = cloudinaryStorageService.extractPublicIdFromUrl(mission.getIconUrl());
        if (iconPublicId != null) {
            cloudinaryStorageService.deleteFile(iconPublicId);
        }
        
        String imagePublicId = cloudinaryStorageService.extractPublicIdFromUrl(mission.getImageUrl());
        if (imagePublicId != null) {
            cloudinaryStorageService.deleteFile(imagePublicId);
        }

        missionRepository.delete(mission);
    }

    private Mission saveOrUpdateMission(Mission mission, MissionRequestDTO missionDTO) {
        // Gérer l'upload de l'icône
        String iconUrl = mission.getIconUrl();
        if (missionDTO.getIconFile() != null && !missionDTO.getIconFile().isEmpty()) {
            // Supprimer l'ancienne icône si elle existe
            if (iconUrl != null && !iconUrl.isEmpty()) {
                String oldIconPublicId = cloudinaryStorageService.extractPublicIdFromUrl(iconUrl);
                if (oldIconPublicId != null) {
                    cloudinaryStorageService.deleteFile(oldIconPublicId);
                }
            }
            // Upload nouvelle icône
            iconUrl = cloudinaryStorageService.uploadImage(missionDTO.getIconFile(), "mission/icons");
        }

        // Gérer l'upload de l'image
        String imageUrl = mission.getImageUrl();
        if (missionDTO.getImageFile() != null && !missionDTO.getImageFile().isEmpty()) {
            // Supprimer l'ancienne image si elle existe
            if (imageUrl != null && !imageUrl.isEmpty()) {
                String oldImagePublicId = cloudinaryStorageService.extractPublicIdFromUrl(imageUrl);
                if (oldImagePublicId != null) {
                    cloudinaryStorageService.deleteFile(oldImagePublicId);
                }
            }
            // Upload nouvelle image
            imageUrl = cloudinaryStorageService.uploadImage(missionDTO.getImageFile(), "mission/images");
        }

        // Mapper les champs
        mission.setTitreFr(missionDTO.getTitreFr());
        mission.setTitreEn(missionDTO.getTitreEn());
        mission.setSloganFr(missionDTO.getSloganFr());
        mission.setSloganEn(missionDTO.getSloganEn());
        mission.setDescriptionFr(missionDTO.getDescriptionFr());
        mission.setDescriptionEn(missionDTO.getDescriptionEn());
        mission.setObjectifsFr(missionDTO.getObjectifsFr());
        mission.setObjectifsEn(missionDTO.getObjectifsEn());
        mission.setValeursFr(missionDTO.getValeursFr());
        mission.setValeursEn(missionDTO.getValeursEn());
        mission.setOrdreAffichage(missionDTO.getOrdreAffichage());
        mission.setActif(missionDTO.getActif() != null ? missionDTO.getActif() : true);
        mission.setIconUrl(iconUrl);
        mission.setImageUrl(imageUrl);

        return missionRepository.save(mission);
    }
}
