package com.spring.spring.controller;

import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/api/debug")
public class DebugController {
    
    @GetMapping("/files")
    public Map<String, Object> listFiles() {
        Map<String, Object> result = new HashMap<>();

        // 1. Répertoire courant de l'application
        File currentDir = new File(".");
        result.put("currentDir", currentDir.getAbsolutePath());
        result.put("currentDirContent", listFilesInDir(currentDir));

        // 2. Vérifier si uploads existe à différents endroits possibles
        String[] pathsToCheck = {
            "uploads",
            "./uploads",
            "/app/uploads",
            "target/uploads",
            "src/main/resources/static/uploads",
            "uploads/projet",
            "uploads/mission"
        };

        Map<String, Object> uploadsCheck = new HashMap<>();
        for (String path : pathsToCheck) {
            File dir = new File(path);
            uploadsCheck.put(path, dir.exists() ? listFilesInDir(dir) : "DOSSIER_INTROUVABLE");
        }
        result.put("uploadsCheck", uploadsCheck);

        return result;
    }
    @GetMapping("/files/{dir}")
    public List<String> listFilesInSpecificDir(@PathVariable String dir) {
        File directory = new File("uploads/" + dir);
        if (!directory.exists() || !directory.isDirectory()) {
            return Arrays.asList("Dossier introuvable : uploads/" + dir);
        }
        String[] files = directory.list();
        return files != null ? Arrays.asList(files) : new ArrayList<>();
    }

    private List<String> listFilesInDir(File dir) {
        if (!dir.exists() || !dir.isDirectory()) return new ArrayList<>();
        String[] files = dir.list();
        return files != null ? Arrays.asList(files) : new ArrayList<>();
    }
}
