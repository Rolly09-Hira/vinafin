package com.spring.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(uploadDir);
            Files.createDirectories(uploadDir.resolve("projet"));
            Files.createDirectories(uploadDir.resolve("partenaire"));
            Files.createDirectories(uploadDir.resolve("actualite"));
            Files.createDirectories(uploadDir.resolve("temoignage"));
            Files.createDirectories(uploadDir.resolve("temoignage/videos"));
            Files.createDirectories(uploadDir.resolve("mission"));
            Files.createDirectories(uploadDir.resolve("utilisateur"));
            Files.createDirectories(uploadDir.resolve("personnel")); // AJOUTÉ
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la création des dossiers uploads", e);
        }
    }

    // Méthode pour images (2 paramètres) - ANCIENNE VERSION COMPATIBLE
    public String storeFile(MultipartFile file, String subdirectory) {
        return storeFile(file, subdirectory, "image");
    }

    // Méthode avec spécification du type (3 paramètres) - NOUVELLE VERSION
    public String storeFile(MultipartFile file, String subdirectory, String fileType) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            }

            // Validation des extensions
            if ("video".equals(fileType)) {
                if (!fileExtension.matches("\\.(mp4|avi|mov|wmv|flv|mkv|webm)$")) {
                    throw new RuntimeException("Format vidéo non supporté. Utilisez MP4, AVI, MOV, WMV, FLV, MKV ou WEBM.");
                }
                subdirectory = subdirectory + "/videos";
            } else if ("image".equals(fileType)) {
                if (!fileExtension.matches("\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
                    throw new RuntimeException("Format image non supporté. Utilisez JPG, JPEG, PNG, GIF, BMP ou WEBP.");
                }
            }

            String filename = UUID.randomUUID().toString() + fileExtension;
            Path destinationPath = uploadDir.resolve(subdirectory).resolve(filename);
            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            return "uploads/" + subdirectory + "/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du stockage du fichier", e);
        }
    }

    // Méthode pour les photos de profil utilisateur
    public String storeProfilePhoto(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            }

            // Validation des formats d'image
            if (!fileExtension.matches("\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
                throw new RuntimeException("Format d'image non supporté. Utilisez JPG, JPEG, PNG, GIF, BMP ou WEBP.");
            }

            // Validation de la taille (max 5MB pour les photos de profil)
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new RuntimeException("La photo ne doit pas dépasser 5MB");
            }

            String filename = "profile_" + UUID.randomUUID().toString() + fileExtension;
            Path destinationPath = uploadDir.resolve("utilisateur").resolve(filename);
            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            return "uploads/utilisateur/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du stockage de la photo de profil", e);
        }
    }

    // NOUVELLE MÉTHODE: Pour les photos du personnel
    public String storePersonnelPhoto(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            }

            // Validation des formats d'image
            if (!fileExtension.matches("\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
                throw new RuntimeException("Format d'image non supporté. Utilisez JPG, JPEG, PNG, GIF, BMP ou WEBP.");
            }

            // Validation de la taille (max 5MB pour les photos)
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new RuntimeException("La photo ne doit pas dépasser 5MB");
            }

            String filename = "personnel_" + UUID.randomUUID().toString() + fileExtension;
            Path destinationPath = uploadDir.resolve("personnel").resolve(filename);
            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            return "uploads/personnel/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du stockage de la photo du personnel", e);
        }
    }

    // Méthode générique de suppression
    public void deleteFile(String filePath) {
        try {
            if (filePath != null && !filePath.isEmpty()) {
                System.out.println("Tentative de suppression du fichier: " + filePath);

                Path fileToDelete = null;

                // Nettoyer le chemin
                String cleanPath = filePath.replace("\\", "/");

                if (cleanPath.startsWith("uploads/")) {
                    fileToDelete = uploadDir.resolve(cleanPath.substring("uploads/".length()));
                } else if (cleanPath.startsWith("/uploads/")) {
                    fileToDelete = uploadDir.resolve(cleanPath.substring("/uploads/".length()));
                } else if (cleanPath.startsWith("upload/")) {
                    fileToDelete = uploadDir.resolve(cleanPath.substring("upload/".length()));
                } else {
                    fileToDelete = uploadDir.resolve(cleanPath);
                }

                System.out.println("Chemin complet du fichier à supprimer: " + fileToDelete.toString());

                boolean deleted = Files.deleteIfExists(fileToDelete);
                System.out.println("Fichier supprimé: " + deleted);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression du fichier " + filePath + ": " + e.getMessage());
            // Ne pas lever d'exception pour ne pas bloquer la suppression de l'entité
        }
    }

    // Méthode pour supprimer un fichier par sous-dossier et nom (optionnel)
    public void deleteFile(String subdirectory, String filename) {
        if (filename != null && !filename.isEmpty()) {
            String filePath = "uploads/" + subdirectory + "/" + filename;
            deleteFile(filePath);
        }
    }
}