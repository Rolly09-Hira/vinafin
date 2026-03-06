package com.spring.spring.controller;

import com.spring.spring.dto.MissionRequestDTO;
import com.spring.spring.entity.Mission;
import com.spring.spring.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@CrossOrigin(origins = "*")
public class MissionController {

    @Autowired
    private MissionService missionService;

    @GetMapping
    public ResponseEntity<List<Mission>> getAllMissions() {
        List<Mission> missions = missionService.getAllMissions();
        return ResponseEntity.ok(missions);
    }

    @GetMapping("/actives")
    public ResponseEntity<List<Mission>> getMissionsActives() {
        List<Mission> missions = missionService.getMissionsActives();
        return ResponseEntity.ok(missions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mission> getMissionById(@PathVariable Long id) {
        Mission mission = missionService.getMissionById(id);
        return ResponseEntity.ok(mission);
    }

    @PostMapping
    public ResponseEntity<Mission> createMission(@ModelAttribute MissionRequestDTO missionDTO) {
        Mission createdMission = missionService.createMission(missionDTO);
        return new ResponseEntity<>(createdMission, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mission> updateMission(
            @PathVariable Long id,
            @ModelAttribute MissionRequestDTO missionDTO) {
        Mission updatedMission = missionService.updateMission(id, missionDTO);
        return ResponseEntity.ok(updatedMission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        missionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }
}