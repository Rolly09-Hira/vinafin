package com.spring.spring.controller;

import com.spring.spring.dto.LoginRequestDTO;
import com.spring.spring.dto.UtilisateurRequestDTO;
import com.spring.spring.entity.Utilisateur;
import com.spring.spring.repository.UtilisateurRepository;
import com.spring.spring.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"},
        allowCredentials = "true")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Endpoint de test de connexion
    @PostMapping("/test-login")
    public ResponseEntity<?> testLogin(@Valid @RequestBody LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getMotDePasse()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Test connexion réussie");
            response.put("authenticated", true);
            response.put("user", Map.of(
                    "id", utilisateur.getId(),
                    "nom", utilisateur.getNom(),
                    "email", utilisateur.getEmail(),
                    "role", utilisateur.getRole(),
                    "photoUrl", utilisateur.getPhotoUrl(),
                    "actif", utilisateur.getActif()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("authenticated", false);
            response.put("message", "Échec d'authentification: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // Vérifier la session actuelle
    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(@AuthenticationPrincipal User user) {
        Map<String, Object> response = new HashMap<>();

        if (user != null) {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(user.getUsername())
                    .orElse(null);

            if (utilisateur != null) {
                response.put("success", true);
                response.put("authenticated", true);
                response.put("user", Map.of(
                        "id", utilisateur.getId(),
                        "nom", utilisateur.getNom(),
                        "email", utilisateur.getEmail(),
                        "role", utilisateur.getRole(),
                        "photoUrl", utilisateur.getPhotoUrl(),
                        "actif", utilisateur.getActif()
                ));
                return ResponseEntity.ok(response);
            }
        }

        response.put("success", false);
        response.put("authenticated", false);
        response.put("message", "Non authentifié");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Obtenir l'utilisateur courant (alias de check-session pour compatibilité)
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) {
        return checkSession(user);
    }

    // Nouvelle méthode register avec photo
    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<?> register(
            @RequestParam("nom") String nom,
            @RequestParam("email") String email,
            @RequestParam("motDePasse") String motDePasse,
            @RequestParam("role") String role,
            @RequestPart(value = "photoFile", required = false) MultipartFile photoFile,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        Utilisateur currentUtilisateur = utilisateurRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!"ADMIN".equals(currentUtilisateur.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", "Seul un administrateur peut créer des utilisateurs"));
        }

        try {
            if (!role.equals("ADMIN") && !role.equals("EDITEUR")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Rôle invalide. Doit être 'ADMIN' ou 'EDITEUR'"));
            }

            UtilisateurRequestDTO dto = new UtilisateurRequestDTO();
            dto.setNom(nom);
            dto.setEmail(email);
            dto.setMotDePasse(motDePasse);
            dto.setRole(role);
            dto.setPhotoFile(photoFile);

            Utilisateur user = authService.creerUtilisateur(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Utilisateur créé avec succès");
            response.put("user", Map.of(
                    "id", user.getId(),
                    "nom", user.getNom(),
                    "email", user.getEmail(),
                    "role", user.getRole(),
                    "photoUrl", user.getPhotoUrl()
            ));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Mettre à jour le profil avec photo - MODIFIÉ POUR RECONNEXION AUTOMATIQUE
    @PutMapping(value = "/profile", consumes = "multipart/form-data")
    public ResponseEntity<?> updateProfile(
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "email", required = false) String email,
            @RequestPart(value = "photoFile", required = false) MultipartFile photoFile,
            @AuthenticationPrincipal User user,
            HttpServletRequest request) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        try {
            String oldEmail = user.getUsername();
            Utilisateur utilisateur = authService.findByEmail(oldEmail);
            Utilisateur updated = authService.updateProfile(utilisateur.getId(), nom, email, photoFile);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profil mis à jour avec succès");
            response.put("user", Map.of(
                    "id", updated.getId(),
                    "nom", updated.getNom(),
                    "email", updated.getEmail(),
                    "role", updated.getRole(),
                    "photoUrl", updated.getPhotoUrl(),
                    "actif", updated.getActif()
            ));

            // SI L'EMAIL A CHANGÉ, RECONNECTER AUTOMATIQUEMENT L'UTILISATEUR
            if (!oldEmail.equals(updated.getEmail())) {
                // Créer une nouvelle authentification avec le nouvel email
                UsernamePasswordAuthenticationToken newAuth =
                        new UsernamePasswordAuthenticationToken(
                                updated.getEmail(),
                                null, // Pas besoin du mot de passe
                                user.getAuthorities()
                        );

                // Mettre à jour le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(newAuth);

                // Changer l'ID de session pour des raisons de sécurité
                request.changeSessionId();

                response.put("emailChanged", true);
                response.put("requireRelogin", false); // L'utilisateur reste connecté
            }

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Changer le mot de passe - MODIFIÉ POUR DÉCONNEXION SÉCURISÉE
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal User user,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        String nouveauMotDePasse = request.get("nouveauMotDePasse");
        if (nouveauMotDePasse == null || nouveauMotDePasse.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Nouveau mot de passe requis"));
        }

        if (nouveauMotDePasse.length() < 6) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Le mot de passe doit avoir au moins 6 caractères"));
        }

        try {
            authService.changerMotDePasse(user.getUsername(), nouveauMotDePasse);

            // DÉCONNECTER L'UTILISATEUR POUR DES RAISONS DE SÉCURITÉ
            SecurityContextHolder.clearContext();
            httpRequest.getSession().invalidate();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Mot de passe changé avec succès. Veuillez vous reconnecter.",
                    "requireRelogin", true
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Lister tous les utilisateurs (ADMIN seulement)
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        try {
            Utilisateur currentUser = authService.findByEmail(user.getUsername());
            if (!"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "message", "Accès refusé"));
            }

            List<Utilisateur> users = utilisateurRepository.findAll();

            List<Map<String, Object>> usersResponse = users.stream()
                    .map(u -> {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("id", u.getId());
                        userMap.put("nom", u.getNom());
                        userMap.put("email", u.getEmail());
                        userMap.put("role", u.getRole());
                        userMap.put("photoUrl", u.getPhotoUrl());
                        userMap.put("actif", u.getActif());
                        userMap.put("createdAt", u.getCreatedAt());
                        userMap.put("updatedAt", u.getUpdatedAt());
                        return userMap;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("users", usersResponse);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Mettre à jour un utilisateur (ADMIN seulement) - MODIFIÉ POUR GÉRER L'EMAIL DE L'UTILISATEUR CONNECTÉ
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal User currentUser,
            HttpServletRequest httpRequest) {

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        try {
            Utilisateur currentUtilisateur = authService.findByEmail(currentUser.getUsername());
            if (!"ADMIN".equals(currentUtilisateur.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "message", "Accès refusé"));
            }

            String nom = (String) request.get("nom");
            String email = (String) request.get("email");
            String role = (String) request.get("role");
            Boolean actif = (Boolean) request.get("actif");

            Utilisateur updated = authService.updateUser(id, nom, email, role, actif);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Utilisateur mis à jour avec succès");
            response.put("user", Map.of(
                    "id", updated.getId(),
                    "nom", updated.getNom(),
                    "email", updated.getEmail(),
                    "role", updated.getRole(),
                    "photoUrl", updated.getPhotoUrl() != null ? updated.getPhotoUrl() : "",
                    "actif", updated.getActif()
            ));

            // SI L'UTILISATEUR MODIFIE SON PROPRE COMPTE ET CHANGE SON EMAIL
            if (currentUtilisateur.getId().equals(id) && !currentUtilisateur.getEmail().equals(updated.getEmail())) {
                // Mettre à jour l'authentification
                UsernamePasswordAuthenticationToken newAuth =
                        new UsernamePasswordAuthenticationToken(
                                updated.getEmail(),
                                null,
                                currentUser.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(newAuth);
                httpRequest.changeSessionId();

                response.put("selfUpdate", true);
                response.put("emailChanged", true);
            }

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Mettre à jour un utilisateur avec photo - MODIFIÉ POUR GÉRER L'EMAIL DE L'UTILISATEUR CONNECTÉ
    @PutMapping(value = "/users/{id}/with-photo", consumes = "multipart/form-data")
    public ResponseEntity<?> updateUserWithPhoto(
            @PathVariable Long id,
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "actif", required = false) Boolean actif,
            @RequestPart(value = "photoFile", required = false) MultipartFile photoFile,
            @AuthenticationPrincipal User currentUser,
            HttpServletRequest httpRequest) {

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        try {
            Utilisateur currentUtilisateur = authService.findByEmail(currentUser.getUsername());
            if (!"ADMIN".equals(currentUtilisateur.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "message", "Accès refusé"));
            }

            Utilisateur updated = authService.updateUserWithPhoto(id, nom, email, role, actif, photoFile);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Utilisateur mis à jour avec succès");
            response.put("user", Map.of(
                    "id", updated.getId(),
                    "nom", updated.getNom(),
                    "email", updated.getEmail(),
                    "role", updated.getRole(),
                    "photoUrl", updated.getPhotoUrl() != null ? updated.getPhotoUrl() : "",
                    "actif", updated.getActif()
            ));

            // SI L'UTILISATEUR MODIFIE SON PROPRE COMPTE ET CHANGE SON EMAIL
            if (currentUtilisateur.getId().equals(id) && !currentUtilisateur.getEmail().equals(updated.getEmail())) {
                // Mettre à jour l'authentification
                UsernamePasswordAuthenticationToken newAuth =
                        new UsernamePasswordAuthenticationToken(
                                updated.getEmail(),
                                null,
                                currentUser.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(newAuth);
                httpRequest.changeSessionId();

                response.put("selfUpdate", true);
                response.put("emailChanged", true);
            }

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Désactiver un utilisateur (ADMIN seulement)
    @PostMapping("/users/{id}/desactivate")
    public ResponseEntity<?> desactivateUser(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        try {
            Utilisateur currentUtilisateur = authService.findByEmail(currentUser.getUsername());
            if (!"ADMIN".equals(currentUtilisateur.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "message", "Accès refusé"));
            }

            if (currentUtilisateur.getId().equals(id)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Vous ne pouvez pas désactiver votre propre compte"));
            }

            authService.desactiverUtilisateur(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Utilisateur désactivé"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Activer un utilisateur (ADMIN seulement)
    @PostMapping("/users/{id}/activate")
    public ResponseEntity<?> activateUser(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Non authentifié"));
        }

        try {
            Utilisateur currentUtilisateur = authService.findByEmail(currentUser.getUsername());
            if (!"ADMIN".equals(currentUtilisateur.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "message", "Accès refusé"));
            }

            Utilisateur utilisateur = utilisateurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            utilisateur.setActif(true);
            utilisateur.setUpdatedAt(LocalDateTime.now());
            utilisateurRepository.save(utilisateur);

            return ResponseEntity.ok(Map.of("success", true, "message", "Utilisateur activé"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Endpoint de vérification
    @GetMapping("/check")
    public ResponseEntity<?> check() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "API auth fonctionnelle");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("totalUsers", utilisateurRepository.count());
        response.put("authenticated", SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        return ResponseEntity.ok(response);
    }
}