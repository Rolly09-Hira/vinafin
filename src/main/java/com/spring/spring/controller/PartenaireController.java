package com.spring.spring.controller;

import com.spring.spring.dto.PartenaireRequestDTO;
import com.spring.spring.entity.Partenaire;
import com.spring.spring.service.PartenaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partenaires")
@CrossOrigin(origins = "*")
public class PartenaireController {

    @Autowired
    private PartenaireService partenaireService;

    @GetMapping
    public ResponseEntity<List<Partenaire>> getAllPartenaires() {
        List<Partenaire> partenaires = partenaireService.getAllPartenaires();
        return ResponseEntity.ok(partenaires);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partenaire> getPartenaireById(@PathVariable Long id) {
        Partenaire partenaire = partenaireService.getPartenaireById(id);
        return ResponseEntity.ok(partenaire);
    }

    @PostMapping
    public ResponseEntity<Partenaire> createPartenaire(@ModelAttribute PartenaireRequestDTO partenaireDTO) {
        Partenaire createdPartenaire = partenaireService.createPartenaire(partenaireDTO);
        return new ResponseEntity<>(createdPartenaire, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partenaire> updatePartenaire(
            @PathVariable Long id,
            @ModelAttribute PartenaireRequestDTO partenaireDTO) {
        Partenaire updatedPartenaire = partenaireService.updatePartenaire(id, partenaireDTO);
        return ResponseEntity.ok(updatedPartenaire);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartenaire(@PathVariable Long id) {
        partenaireService.deletePartenaire(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<Partenaire>> getPartenairesActifs() {
        List<Partenaire> partenaires = partenaireService.getAllPartenaires()
                .stream()
                .filter(Partenaire::getActif)
                .toList();
        return ResponseEntity.ok(partenaires);
    }
}