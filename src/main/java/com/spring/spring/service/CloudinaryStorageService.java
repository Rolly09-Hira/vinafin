package com.spring.spring.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryStorageService {

    private final Cloudinary cloudinary;

    /**
     * Upload une image vers Cloudinary
     * @param file Le fichier à uploader
     * @param folder Le dossier (ex: "projet", "actualite", "personnel")
     * @return L'URL sécurisée de l'image
     */
    public String uploadImage(MultipartFile file, String folder) {
        try {
            // Validation basique
            if (file == null || file.isEmpty()) {
                return null;
            }

            // Vérifier le type de fichier
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("Le fichier doit être une image");
            }

            // Options d'upload
            Map<String, Object> options = ObjectUtils.asMap(
                "folder", "vina/" + folder,
                "public_id", generatePublicId(file.getOriginalFilename()),
                "overwrite", true,
                "resource_type", "image"
            );

            // Upload vers Cloudinary
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            
            // Retourner l'URL sécurisée
            return (String) uploadResult.get("secure_url");

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload vers Cloudinary: " + e.getMessage(), e);
        }
    }

    /**
     * Upload une vidéo vers Cloudinary
     * @param file Le fichier vidéo à uploader
     * @param folder Le dossier (ex: "temoignage")
     * @return L'URL sécurisée de la vidéo
     */
    public String uploadVideo(MultipartFile file, String folder) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }

            // Vérifier le type de fichier
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("video/")) {
                throw new RuntimeException("Le fichier doit être une vidéo");
            }

            Map<String, Object> options = ObjectUtils.asMap(
                "folder", "vina/" + folder,
                "public_id", generatePublicId(file.getOriginalFilename()),
                "overwrite", true,
                "resource_type", "video"
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            return (String) uploadResult.get("secure_url");

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload de la vidéo vers Cloudinary: " + e.getMessage(), e);
        }
    }

    /**
     * Upload un fichier (détection automatique du type)
     */
    public String uploadFile(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String contentType = file.getContentType();
        if (contentType != null && contentType.startsWith("video/")) {
            return uploadVideo(file, folder);
        } else {
            return uploadImage(file, folder);
        }
    }

    /**
     * Supprime un fichier de Cloudinary
     * @param publicId L'ID public du fichier (extrait de l'URL)
     */
    public void deleteFile(String publicId) {
        try {
            if (publicId != null && !publicId.isEmpty()) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression du fichier Cloudinary: " + e.getMessage());
        }
    }

    /**
     * Extrait le public ID d'une URL Cloudinary
     */
    public String extractPublicIdFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        
        // Format typique: https://res.cloudinary.com/.../v123456/vina/folder/nomfichier
        try {
            // Supprimer tout après le dernier point (extension)
            String withoutExtension = url.replaceFirst("\\.[^/.]+$", "");
            // Prendre tout après "/upload/"
            String[] parts = withoutExtension.split("/upload/");
            if (parts.length > 1) {
                return parts[1].replaceFirst("^v[0-9]+/", "");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'extraction du publicId: " + e.getMessage());
        }
        return null;
    }

    /**
     * Génère un ID public unique
     */
    private String generatePublicId(String originalFilename) {
        String base = UUID.randomUUID().toString();
        if (originalFilename != null && !originalFilename.isEmpty()) {
            // Nettoyer le nom original des caractères spéciaux
            String cleanName = originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");
            base = UUID.randomUUID().toString() + "_" + cleanName;
        }
        return base;
    }
}
