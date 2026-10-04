package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.ExperienceLevel;
import com.ascendingcloud.staffing.service.ExperienceLevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/experience-levels")
@CrossOrigin(origins = "*")
public class ExperienceLevelController {

    private final ExperienceLevelService experienceLevelService;

    public ExperienceLevelController(
            ExperienceLevelService experienceLevelService) {
        this.experienceLevelService = experienceLevelService;
    }

    @GetMapping
    public ResponseEntity<List<ExperienceLevel>>
    getAllExperienceLevels() {

        return ResponseEntity.ok(
                experienceLevelService.getAllExperienceLevels()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceLevel>
    getExperienceLevelById(@PathVariable Integer id) {

        Optional<ExperienceLevel> result =
                experienceLevelService.getExperienceLevelById(id);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result.get());
    }

    @PostMapping
    public ResponseEntity<ExperienceLevel>
    createExperienceLevel(
            @RequestBody ExperienceLevel experienceLevel) {

        return ResponseEntity.ok(
                experienceLevelService
                        .createExperienceLevel(experienceLevel)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceLevel>
    updateExperienceLevel(
            @PathVariable Integer id,
            @RequestBody ExperienceLevel experienceLevel) {

        ExperienceLevel updated =
                experienceLevelService.updateExperienceLevel(
                        id,
                        experienceLevel
                );

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteExperienceLevel(@PathVariable Integer id) {

        if (!experienceLevelService
                .deleteExperienceLevel(id)) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}