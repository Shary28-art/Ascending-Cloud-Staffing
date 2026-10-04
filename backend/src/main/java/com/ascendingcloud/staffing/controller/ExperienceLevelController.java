package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.ExperienceLevel;
import com.ascendingcloud.staffing.service.ExperienceLevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experience-levels")
public class ExperienceLevelController {

    private final ExperienceLevelService service;

    public ExperienceLevelController(ExperienceLevelService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ExperienceLevel>> getAllExperienceLevels() {
        return ResponseEntity.ok(service.getAllExperienceLevels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceLevel> getExperienceLevelById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getExperienceLevelById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExperienceLevel> createExperienceLevel(@RequestBody ExperienceLevel body) {
        return ResponseEntity.status(201).body(service.createExperienceLevel(body));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExperienceLevel> updateExperienceLevel(
            @PathVariable Integer id,
            @RequestBody ExperienceLevel body) {
        return ResponseEntity.ok(service.updateExperienceLevel(id, body));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteExperienceLevel(@PathVariable Integer id) {
        service.deleteExperienceLevel(id);
        return ResponseEntity.noContent().build();
    }
}
