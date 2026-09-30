package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.ExperienceLevel;
import com.ascendingcloud.staffing.service.ExperienceLevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experience-levels")
@CrossOrigin(origins = "*")
public class ExperienceLevelController {

    private final ExperienceLevelService experienceLevelService;

    public ExperienceLevelController(
            ExperienceLevelService experienceLevelService) {
        this.experienceLevelService = experienceLevelService;
    }

    @GetMapping
    public ResponseEntity<List<ExperienceLevel>> getAllExperienceLevels() {
        return ResponseEntity.ok(
                experienceLevelService.getAllExperienceLevels()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceLevel> getExperienceLevelById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                experienceLevelService.getExperienceLevelById(id)
        );
    }

    @PostMapping
    public ResponseEntity<ExperienceLevel> createExperienceLevel(
            @RequestBody ExperienceLevel experienceLevel) {
        return ResponseEntity.ok(
                experienceLevelService.createExperienceLevel(
                        experienceLevel
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceLevel> updateExperienceLevel(
            @PathVariable Integer id,
            @RequestBody ExperienceLevel experienceLevel) {
        return ResponseEntity.ok(
                experienceLevelService.updateExperienceLevel(
                        id, experienceLevel
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperienceLevel(
            @PathVariable Integer id) {
        experienceLevelService.deleteExperienceLevel(id);
        return ResponseEntity.noContent().build();
    }
}