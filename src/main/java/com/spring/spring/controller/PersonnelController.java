package com.spring.spring.controller;

import com.spring.spring.dto.PersonnelRequestDTO;
import com.spring.spring.entity.Personnel;
import com.spring.spring.service.PersonnelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/personnel")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"},
        allowCredentials = "true")
public class PersonnelController {

    @Autowired
    private PersonnelService personnelService;

    // GET - Récupérer tout le personnel (PUBLIC)
    @GetMapping
    public ResponseEntity<?> getAllPersonnel() {
        try {
            List<Personnel> personnelList = personnelService.getAllPersonnel();

            List<Map<String, Object>> responseList = personnelList.stream()
                    .map(this::createPersonnelMap)
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", responseList.size());
            response.put("personnel", responseList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // GET - Récupérer un membre par ID (PUBLIC)
    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonnelById(@PathVariable Long id) {
        try {
            Personnel personnel = personnelService.getPersonnelById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("personnel", createPersonnelMap(personnel));

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // GET - Récupérer par département (PUBLIC)
    @GetMapping("/departement/{departement}")
    public ResponseEntity<?> getPersonnelByDepartement(@PathVariable String departement) {
        try {
            List<Personnel> personnelList = personnelService.getPersonnelByDepartement(departement);

            List<Map<String, Object>> responseList = personnelList.stream()
                    .map(this::createPersonnelMap)
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", responseList.size());
            response.put("personnel", responseList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // POST - Créer un nouveau membre (PROTÉGÉ)
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createPersonnel(
            @Valid @ModelAttribute PersonnelRequestDTO dto,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        try {
            Personnel personnel = personnelService.createPersonnel(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Membre du personnel créé avec succès");
            response.put("personnel", createPersonnelMap(personnel));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // PUT - Mettre à jour un membre (PROTÉGÉ)
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updatePersonnel(
            @PathVariable Long id,
            @Valid @ModelAttribute PersonnelRequestDTO dto,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        try {
            Personnel personnel = personnelService.updatePersonnel(id, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Membre du personnel mis à jour avec succès");
            response.put("personnel", createPersonnelMap(personnel));

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // DELETE - Supprimer un membre (PROTÉGÉ - ADMIN SEULEMENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePersonnel(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        try {
            personnelService.deletePersonnel(id);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Membre du personnel supprimé avec succès"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // GET - Compter le personnel (PUBLIC)
    @GetMapping("/count")
    public ResponseEntity<?> countPersonnel() {
        try {
            long count = personnelService.countPersonnel();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Méthode utilitaire pour créer un Map sécurisé du personnel
    private Map<String, Object> createPersonnelMap(Personnel personnel) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", personnel.getId());
        map.put("nom", personnel.getNom() != null ? personnel.getNom() : "");
        map.put("prenom", personnel.getPrenom() != null ? personnel.getPrenom() : "");
        map.put("email", personnel.getEmail() != null ? personnel.getEmail() : "");
        map.put("telephone", personnel.getTelephone() != null ? personnel.getTelephone() : "");
        map.put("poste", personnel.getPoste() != null ? personnel.getPoste() : "");
        map.put("departement", personnel.getDepartement() != null ? personnel.getDepartement() : "");
        map.put("dateEmbauche", personnel.getDateEmbauche() != null ? personnel.getDateEmbauche().toString() : null);
        map.put("biographieFr", personnel.getBiographieFr() != null ? personnel.getBiographieFr() : "");
        map.put("biographieEn", personnel.getBiographieEn() != null ? personnel.getBiographieEn() : "");
        map.put("specialites", personnel.getSpecialites() != null ? personnel.getSpecialites() : "");
        map.put("photoUrl", personnel.getPhotoUrl() != null ? personnel.getPhotoUrl() : "");
        map.put("linkedinUrl", personnel.getLinkedinUrl() != null ? personnel.getLinkedinUrl() : "");
        map.put("twitterUrl", personnel.getTwitterUrl() != null ? personnel.getTwitterUrl() : "");
        map.put("facebookUrl", personnel.getFacebookUrl() != null ? personnel.getFacebookUrl() : "");
        map.put("ordreAffichage", personnel.getOrdreAffichage() != null ? personnel.getOrdreAffichage() : 0);
        map.put("createdAt", personnel.getCreatedAt() != null ? personnel.getCreatedAt().toString() : null);
        map.put("updatedAt", personnel.getUpdatedAt() != null ? personnel.getUpdatedAt().toString() : null);
        return map;
    }
}