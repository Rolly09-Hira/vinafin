package com.spring.spring.controller;

import com.spring.spring.dto.ProjetRequestDTO;
import com.spring.spring.entity.Projet;
import com.spring.spring.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@CrossOrigin(origins = "*")
public class ProjetController {

    @Autowired
    private ProjetService projetService;

    @GetMapping
    public ResponseEntity<List<Projet>> getAllProjets() {
        List<Projet> projets = projetService.getAllProjets();
        return ResponseEntity.ok(projets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projet> getProjetById(@PathVariable Long id) {
        Projet projet = projetService.getProjetById(id);
        return ResponseEntity.ok(projet);
    }

    @PostMapping
    public ResponseEntity<Projet> createProjet(@ModelAttribute ProjetRequestDTO projetDTO) {
        Projet createdProjet = projetService.createProjet(projetDTO);
        return new ResponseEntity<>(createdProjet, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Projet> updateProjet(
            @PathVariable Long id,
            @ModelAttribute ProjetRequestDTO projetDTO) {
        Projet updatedProjet = projetService.updateProjet(id, projetDTO);
        return ResponseEntity.ok(updatedProjet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        projetService.deleteProjet(id);
        return ResponseEntity.noContent().build();
    }
}