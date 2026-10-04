package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.JobDTO;
import com.ascendingcloud.staffing.dto.StatusUpdateRequest;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private final CurrentUserService currentUserService;

    public JobController(
            JobService jobService,
            CurrentUserService currentUserService) {
        this.jobService = jobService;
        this.currentUserService = currentUserService;
    }

    /** Public job board: only admin-approved, active jobs. */
    @GetMapping
    public ResponseEntity<List<JobDTO>> getPublicJobs() {
        return ResponseEntity.ok(jobService.searchPublicJobs(null, null));
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobDTO>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(jobService.searchPublicJobs(title, location));
    }

    /** Every job (any approval state) of the logged-in employer's company. */
    @GetMapping("/mine")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<List<JobDTO>> getMyJobs() {
        return ResponseEntity.ok(jobService.getMyCompanyJobs(currentUserService.getUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJob(@PathVariable Integer id) {
        return ResponseEntity.ok(jobService.getJob(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<JobDTO> createJob(@RequestBody JobDTO request) {
        return ResponseEntity.status(201).body(
                jobService.createJob(currentUserService.getUser(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<JobDTO> updateJob(
            @PathVariable Integer id,
            @RequestBody JobDTO request) {
        return ResponseEntity.ok(
                jobService.updateJob(currentUserService.getUser(), id, request));
    }

    /** Body: {"status": "PAUSED"}. One of DRAFT, ACTIVE, PAUSED, FILLED, CLOSED, EXPIRED. */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<JobDTO> updateStatus(
            @PathVariable Integer id,
            @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(
                jobService.updateJobStatus(currentUserService.getUser(), id, request.status()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<Void> deleteJob(@PathVariable Integer id) {
        jobService.deleteJob(currentUserService.getUser(), id);
        return ResponseEntity.noContent().build();
    }
}
