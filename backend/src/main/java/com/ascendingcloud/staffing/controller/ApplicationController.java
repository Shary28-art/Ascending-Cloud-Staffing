package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.ApplicationDTO;
import com.ascendingcloud.staffing.dto.ApplicationResponse;
import com.ascendingcloud.staffing.dto.ApplicationStatusHistoryResponse;
import com.ascendingcloud.staffing.dto.StatusUpdateRequest;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final CurrentUserService currentUserService;

    public ApplicationController(
            ApplicationService applicationService,
            CurrentUserService currentUserService) {
        this.applicationService = applicationService;
        this.currentUserService = currentUserService;
    }

    /** Candidate applies. Body: {"jobId": 12}. */
    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponse> apply(@RequestBody ApplicationDTO request) {
        return ResponseEntity.status(201).body(
                applicationService.apply(currentUserService.getUser(), request.getJobId()));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {
        return ResponseEntity.ok(
                applicationService.getMyApplications(currentUserService.getUser()));
    }

    /** Employers see their company's applications; admins see all. */
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<List<ApplicationResponse>> getAll() {
        return ResponseEntity.ok(applicationService.getAll(currentUserService.getUser()));
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<List<ApplicationResponse>> getJobApplications(
            @PathVariable Integer jobId) {
        return ResponseEntity.ok(
                applicationService.getJobApplications(currentUserService.getUser(), jobId));
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<String>> getStatuses() {
        return ResponseEntity.ok(applicationService.getAllowedStatuses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(applicationService.getById(currentUserService.getUser(), id));
    }

    /** Body: {"status": "Shortlisted"}. Candidates may only send "Withdrawn". */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Integer id,
            @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(applicationService.updateStatus(
                currentUserService.getUser(), id, request.status()));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ApplicationStatusHistoryResponse>> getHistory(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                applicationService.getStatusHistory(currentUserService.getUser(), id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        applicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
