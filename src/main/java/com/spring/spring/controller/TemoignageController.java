package com.spring.spring.controller;

import com.spring.spring.dto.TemoignageRequestDTO;
import com.spring.spring.entity.Temoignage;
import com.spring.spring.service.TemoignageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/temoignages")
@CrossOrigin(origins = "*")
public class TemoignageController {

    @Autowired
    private TemoignageService temoignageService;

    @GetMapping
    public ResponseEntity<List<Temoignage>> getAllTemoignages() {
        List<Temoignage> temoignages = temoignageService.getAllTemoignages();
        return ResponseEntity.ok(temoignages);
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<Temoignage>> getTemoignagesActifs() {
        List<Temoignage> temoignages = temoignageService.getTemoignagesActifs();
        return ResponseEntity.ok(temoignages);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Temoignage>> getTemoignagesByType(@PathVariable String type) {
        List<Temoignage> temoignages = temoignageService.getTemoignagesByType(type);
        return ResponseEntity.ok(temoignages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Temoignage> getTemoignageById(@PathVariable Long id) {
        Temoignage temoignage = temoignageService.getTemoignageById(id);
        return ResponseEntity.ok(temoignage);
    }

    @PostMapping
    public ResponseEntity<Temoignage> createTemoignage(@ModelAttribute TemoignageRequestDTO temoignageDTO) {
        Temoignage createdTemoignage = temoignageService.createTemoignage(temoignageDTO);
        return new ResponseEntity<>(createdTemoignage, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Temoignage> updateTemoignage(
            @PathVariable Long id,
            @ModelAttribute TemoignageRequestDTO temoignageDTO) {
        Temoignage updatedTemoignage = temoignageService.updateTemoignage(id, temoignageDTO);
        return ResponseEntity.ok(updatedTemoignage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemoignage(@PathVariable Long id) {
        temoignageService.deleteTemoignage(id);
        return ResponseEntity.noContent().build();
    }
}