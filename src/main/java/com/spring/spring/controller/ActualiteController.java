package com.spring.spring.controller;

import com.spring.spring.dto.ActualiteRequestDTO;
import com.spring.spring.entity.Actualite;
import com.spring.spring.service.ActualiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actualites")
@CrossOrigin(origins = "*")
public class ActualiteController {

    @Autowired
    private ActualiteService actualiteService;

    @GetMapping
    public ResponseEntity<List<Actualite>> getAllActualites() {
        List<Actualite> actualites = actualiteService.getAllActualites();
        return ResponseEntity.ok(actualites);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actualite> getActualiteById(@PathVariable Long id) {
        Actualite actualite = actualiteService.getActualiteById(id);
        return ResponseEntity.ok(actualite);
    }

    @GetMapping("/importantes")
    public ResponseEntity<List<Actualite>> getActualitesImportantes() {
        List<Actualite> actualites = actualiteService.getActualitesImportantes();
        return ResponseEntity.ok(actualites);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Actualite>> getActualitesByType(@PathVariable String type) {
        List<Actualite> actualites = actualiteService.getActualitesByType(type);
        return ResponseEntity.ok(actualites);
    }

    @PostMapping
    public ResponseEntity<Actualite> createActualite(@ModelAttribute ActualiteRequestDTO actualiteDTO) {
        Actualite createdActualite = actualiteService.createActualite(actualiteDTO);
        return new ResponseEntity<>(createdActualite, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actualite> updateActualite(
            @PathVariable Long id,
            @ModelAttribute ActualiteRequestDTO actualiteDTO) {
        Actualite updatedActualite = actualiteService.updateActualite(id, actualiteDTO);
        return ResponseEntity.ok(updatedActualite);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActualite(@PathVariable Long id) {
        actualiteService.deleteActualite(id);
        return ResponseEntity.noContent().build();
    }
}