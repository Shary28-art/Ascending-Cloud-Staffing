package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.SavedJobDTO;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.SavedJobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-jobs")
@PreAuthorize("hasRole('CANDIDATE')")
public class SavedJobController {

    private final SavedJobService savedJobService;
    private final CurrentUserService currentUserService;

    public SavedJobController(
            SavedJobService savedJobService,
            CurrentUserService currentUserService) {
        this.savedJobService = savedJobService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<List<SavedJobDTO>> getMySavedJobs() {
        return ResponseEntity.ok(savedJobService.getMySavedJobs(currentUserService.getUser()));
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<SavedJobDTO> saveJob(@PathVariable Integer jobId) {
        return ResponseEntity.status(201).body(
                savedJobService.saveJob(currentUserService.getUser(), jobId));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> unsaveJob(@PathVariable Integer jobId) {
        savedJobService.unsaveJob(currentUserService.getUser(), jobId);
        return ResponseEntity.noContent().build();
    }
}
