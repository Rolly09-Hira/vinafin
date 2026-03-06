package com.spring.spring.controller;

import com.spring.spring.dto.DonIntentionRequestDTO;
import com.spring.spring.dto.DonIntentionResponseDTO;
import com.spring.spring.service.DonIntentionService;
import com.spring.spring.service.GeolocalisationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dons")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class DonIntentionController {

    @Autowired
    private DonIntentionService donIntentionService;

    @Autowired
    private GeolocalisationService geolocalisationService;

    /**
     * Soumettre une intention de don (public)
     */
    @PostMapping("/intention")
    public ResponseEntity<DonIntentionResponseDTO> createIntention(
            @Valid @RequestBody DonIntentionRequestDTO dto,
            HttpServletRequest request) {

        // Récupération des données techniques depuis la requête
        String ipAddress = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        // Géolocalisation à partir de l'IP (optionnel)
        String pays = geolocalisationService.getPaysFromIp(ipAddress);
        String ville = geolocalisationService.getVilleFromIp(ipAddress);

        DonIntentionResponseDTO response = donIntentionService.createIntention(
                dto, ipAddress, userAgent, pays, ville);

        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer une intention de don (public - page de confirmation)
     */
    @GetMapping("/confirmation/{id}")
    public ResponseEntity<DonIntentionResponseDTO> getConfirmation(@PathVariable Long id) {
        // Version simplifiée pour la page de confirmation publique
        DonIntentionResponseDTO intention = donIntentionService.getIntentionByIdForConfirmation(id);
        return ResponseEntity.ok(intention);
    }

    /**
     * Récupérer l'IP réelle du client (derrière proxy)
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // Si plusieurs IP (X-Forwarded-For peut contenir une liste), prendre la première
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }
}