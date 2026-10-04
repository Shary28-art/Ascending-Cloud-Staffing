package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.SavedJob;
import com.ascendingcloud.staffing.service.SavedJobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-jobs")
@CrossOrigin(origins = "*")
public class SavedJobController {

    private final SavedJobService savedJobService;

    public SavedJobController(SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }

    @GetMapping
    public ResponseEntity<List<SavedJob>> getAllSavedJobs() {
        return ResponseEntity.ok(savedJobService.getAllSavedJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavedJob> getSavedJobById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                savedJobService.getSavedJobById(id)
        );
    }

    @PostMapping
    public ResponseEntity<SavedJob> createSavedJob(
            @RequestBody SavedJob savedJob) {
        return ResponseEntity.ok(
                savedJobService.createSavedJob(savedJob)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSavedJob(
            @PathVariable Integer id) {
        savedJobService.deleteSavedJob(id);
        return ResponseEntity.noContent().build();
    }
}