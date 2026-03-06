package com.spring.spring.controller;

import com.spring.spring.dto.ContactInfoRequestDTO;
import com.spring.spring.entity.ContactInfo;
import com.spring.spring.service.ContactInfoService;
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
@RequestMapping("/api/contact-infos")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ContactInfoController {

    @Autowired
    private ContactInfoService contactInfoService;

    // GET - Public (tout le monde peut voir les contacts)
    @GetMapping
    public ResponseEntity<?> getAllContactInfos() {
        List<ContactInfo> contactInfos = contactInfoService.getAllContactInfos();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", contactInfos);
        response.put("count", contactInfos.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContactInfoById(@PathVariable Long id) {
        return contactInfoService.getById(id)
                .map(contactInfo -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", contactInfo);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "Contact non trouvé")));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getContactInfosByType(@PathVariable String type) {
        List<ContactInfo> contactInfos = contactInfoService.getByType(type);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", contactInfos);
        response.put("type", type);
        response.put("count", contactInfos.size());

        return ResponseEntity.ok(response);
    }

    // Endpoints spécifiques
    @GetMapping("/telephones")
    public ResponseEntity<?> getTelephones() {
        return getContactInfosByType("telephone");
    }

    @GetMapping("/emails")
    public ResponseEntity<?> getEmails() {
        return getContactInfosByType("email");
    }

    @GetMapping("/reseaux-sociaux")
    public ResponseEntity<?> getReseauxSociaux() {
        return getContactInfosByType("reseau_social");
    }

    @GetMapping("/adresses")
    public ResponseEntity<?> getAdresses() {
        return getContactInfosByType("adresse");
    }

    // POST - Seulement ADMIN/EDITEUR
    @PostMapping
    public ResponseEntity<?> createContactInfo(
            @Valid @RequestBody ContactInfoRequestDTO dto,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        try {
            ContactInfo contactInfo = contactInfoService.createContactInfo(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Contact créé avec succès");
            response.put("data", contactInfo);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // PUT - Seulement ADMIN/EDITEUR
    @PutMapping("/{id}")
    public ResponseEntity<?> updateContactInfo(
            @PathVariable Long id,
            @Valid @RequestBody ContactInfoRequestDTO dto,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        try {
            ContactInfo contactInfo = contactInfoService.updateContactInfo(id, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Contact mis à jour avec succès");
            response.put("data", contactInfo);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // DELETE - Seulement ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContactInfo(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        try {
            contactInfoService.deleteContactInfo(id);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Contact supprimé avec succès"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // PATCH - Activer/Désactiver (Seulement ADMIN/EDITEUR)
    @PatchMapping("/{id}/toggle-actif")
    public ResponseEntity<?> toggleActif(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        try {
            ContactInfo contactInfo = contactInfoService.toggleActif(id);

            String status = contactInfo.getActif() ? "activé" : "désactivé";

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Contact " + status + " avec succès");
            response.put("data", contactInfo);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}