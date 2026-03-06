package com.spring.spring.controller;

import com.spring.spring.dto.DonIntentionAdminDTO;
import com.spring.spring.dto.DonIntentionStatistiquesDTO;
import com.spring.spring.entity.DonIntention;
import com.spring.spring.service.DonIntentionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dons")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class DonIntentionAdminController {

    @Autowired
    private DonIntentionService donIntentionService;

    /**
     * Liste paginée de toutes les intentions
     */
    @GetMapping("/intentions")
    public ResponseEntity<Page<DonIntentionAdminDTO>> getAllIntentions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateSoumission") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(donIntentionService.getAllIntentions(pageable));
    }

    /**
     * Détail d'une intention
     */
    @GetMapping("/intentions/{id}")
    public ResponseEntity<DonIntentionAdminDTO> getIntentionById(@PathVariable Long id) {
        return ResponseEntity.ok(donIntentionService.getIntentionById(id));
    }

    /**
     * Mettre à jour le statut d'une intention
     */
    @PutMapping("/intentions/{id}/statut")
    public ResponseEntity<DonIntentionAdminDTO> updateStatut(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {

        String statutStr = (String) payload.get("statut");
        String notes = (String) payload.get("notes");

        DonIntention.StatutDon statut = DonIntention.StatutDon.valueOf(statutStr);

        return ResponseEntity.ok(donIntentionService.updateStatut(id, statut, notes));
    }

    /**
     * Ajouter des notes internes
     */
    @PostMapping("/intentions/{id}/notes")
    public ResponseEntity<?> addNotes(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {

        String notes = payload.get("notes");
        donIntentionService.addNotesInternes(id, notes);

        return ResponseEntity.ok(Map.of("message", "Notes ajoutées avec succès"));
    }
    /**
     * SUPPRIMER UNE INTENTION DE DON (ADMIN)
     * Endpoint pour supprimer définitivement une intention
     */
    @DeleteMapping("/intentions/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Seul l'admin peut supprimer
    public ResponseEntity<?> deleteIntention(@PathVariable Long id) {
        try {
            donIntentionService.deleteIntention(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Intention de don supprimée avec succès"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Erreur lors de la suppression: " + e.getMessage()
            ));
        }
    }
    /**
     * Statistiques pour le dashboard
     */
    @GetMapping("/intentions/stats")
    public ResponseEntity<DonIntentionStatistiquesDTO> getStatistiques() {
        return ResponseEntity.ok(donIntentionService.getStatistiques());
    }
    /**
     * Recherche avancée avec filtres
     */
    @GetMapping("/intentions/recherche")
    public ResponseEntity<Page<DonIntentionAdminDTO>> rechercherIntentions(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) DonIntention.StatutDon statut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("dateSoumission").descending());

        return ResponseEntity.ok(donIntentionService.rechercherAvecFiltres(
                nom, email, statut, dateDebut, dateFin, pageable));
    }
}