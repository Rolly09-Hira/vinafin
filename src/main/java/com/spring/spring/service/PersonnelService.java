package com.spring.spring.service;

import com.spring.spring.dto.PersonnelRequestDTO;
import com.spring.spring.entity.Personnel;
import com.spring.spring.repository.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PersonnelService {

    @Autowired
    private PersonnelRepository personnelRepository;

    @Autowired
    private FileStorageService fileStorageService;

    // Récupérer tout le personnel
    public List<Personnel> getAllPersonnel() {
        return personnelRepository.findAllOrdered();
    }

    // Récupérer un membre par ID
    public Personnel getPersonnelById(Long id) {
        return personnelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membre du personnel non trouvé avec l'id: " + id));
    }

    // Récupérer par email
    public Personnel getPersonnelByEmail(String email) {
        return personnelRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Membre du personnel non trouvé avec l'email: " + email));
    }

    // Créer un nouveau membre
    @Transactional
    public Personnel createPersonnel(PersonnelRequestDTO dto) {
        if (personnelRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Un membre avec cet email existe déjà");
        }

        Personnel personnel = new Personnel();
        personnel.setNom(dto.getNom());
        personnel.setPrenom(dto.getPrenom());
        personnel.setEmail(dto.getEmail());
        personnel.setTelephone(dto.getTelephone());
        personnel.setPoste(dto.getPoste());
        personnel.setDepartement(dto.getDepartement());
        personnel.setDateEmbauche(dto.getDateEmbauche());
        personnel.setBiographieFr(dto.getBiographieFr());
        personnel.setBiographieEn(dto.getBiographieEn());
        personnel.setSpecialites(dto.getSpecialites());
        personnel.setLinkedinUrl(dto.getLinkedinUrl());
        personnel.setTwitterUrl(dto.getTwitterUrl());
        personnel.setFacebookUrl(dto.getFacebookUrl());
        personnel.setOrdreAffichage(dto.getOrdreAffichage());
        personnel.setCreatedAt(LocalDateTime.now());
        personnel.setUpdatedAt(LocalDateTime.now());

        // Gérer l'upload de la photo si présente
        if (dto.getPhotoFile() != null && !dto.getPhotoFile().isEmpty()) {
            String photoUrl = fileStorageService.storePersonnelPhoto(dto.getPhotoFile());
            personnel.setPhotoUrl(photoUrl);
        }

        return personnelRepository.save(personnel);
    }

    // Mettre à jour un membre
    @Transactional
    public Personnel updatePersonnel(Long id, PersonnelRequestDTO dto) {
        Personnel personnel = getPersonnelById(id);

        // Vérifier si l'email est déjà utilisé par un autre membre
        if (dto.getEmail() != null && !dto.getEmail().equals(personnel.getEmail())) {
            if (personnelRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Un membre avec cet email existe déjà");
            }
            personnel.setEmail(dto.getEmail());
        }

        if (dto.getNom() != null) personnel.setNom(dto.getNom());
        if (dto.getPrenom() != null) personnel.setPrenom(dto.getPrenom());
        if (dto.getTelephone() != null) personnel.setTelephone(dto.getTelephone());
        if (dto.getPoste() != null) personnel.setPoste(dto.getPoste());
        if (dto.getDepartement() != null) personnel.setDepartement(dto.getDepartement());
        if (dto.getDateEmbauche() != null) personnel.setDateEmbauche(dto.getDateEmbauche());
        if (dto.getBiographieFr() != null) personnel.setBiographieFr(dto.getBiographieFr());
        if (dto.getBiographieEn() != null) personnel.setBiographieEn(dto.getBiographieEn());
        if (dto.getSpecialites() != null) personnel.setSpecialites(dto.getSpecialites());
        if (dto.getLinkedinUrl() != null) personnel.setLinkedinUrl(dto.getLinkedinUrl());
        if (dto.getTwitterUrl() != null) personnel.setTwitterUrl(dto.getTwitterUrl());
        if (dto.getFacebookUrl() != null) personnel.setFacebookUrl(dto.getFacebookUrl());
        if (dto.getOrdreAffichage() != null) personnel.setOrdreAffichage(dto.getOrdreAffichage());

        // Gérer l'upload de la nouvelle photo
        if (dto.getPhotoFile() != null && !dto.getPhotoFile().isEmpty()) {
            // Supprimer l'ancienne photo si elle existe
            if (personnel.getPhotoUrl() != null && !personnel.getPhotoUrl().isEmpty()) {
                fileStorageService.deleteFile(personnel.getPhotoUrl());
            }
            String newPhotoUrl = fileStorageService.storePersonnelPhoto(dto.getPhotoFile());
            personnel.setPhotoUrl(newPhotoUrl);
        }

        personnel.setUpdatedAt(LocalDateTime.now());
        return personnelRepository.save(personnel);
    }

    // Supprimer un membre
    @Transactional
    public void deletePersonnel(Long id) {
        Personnel personnel = getPersonnelById(id);

        // Supprimer la photo si elle existe
        if (personnel.getPhotoUrl() != null && !personnel.getPhotoUrl().isEmpty()) {
            fileStorageService.deleteFile(personnel.getPhotoUrl());
        }

        personnelRepository.delete(personnel);
    }

    // Récupérer par département
    public List<Personnel> getPersonnelByDepartement(String departement) {
        return personnelRepository.findByDepartementOrderByOrdreAffichageAsc(departement);
    }

    // Compter le nombre total
    public long countPersonnel() {
        return personnelRepository.countTotalPersonnel();
    }
}