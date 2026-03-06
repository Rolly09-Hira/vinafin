package com.spring.spring.controller;

import com.spring.spring.dto.RegionDTO;
import com.spring.spring.service.RegionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@CrossOrigin(origins = "*")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionDTO>> getAllRegions() {
        List<RegionDTO> regions = regionService.getAllRegions();
        return ResponseEntity.ok(regions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> getRegionById(@PathVariable Long id) {
        RegionDTO region = regionService.getRegionById(id);
        return ResponseEntity.ok(region);
    }

    @PostMapping
    public ResponseEntity<RegionDTO> createRegion(@Valid @RequestBody RegionDTO regionDTO) {
        RegionDTO createdRegion = regionService.createRegion(regionDTO);
        return new ResponseEntity<>(createdRegion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO> updateRegion(
            @PathVariable Long id,
            @Valid @RequestBody RegionDTO regionDTO) {
        RegionDTO updatedRegion = regionService.updateRegion(id, regionDTO);
        return ResponseEntity.ok(updatedRegion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
}