package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.JobAlertDTO;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.JobAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Job alerts of the logged-in user. */
@RestController
@RequestMapping("/api/job-alerts")
public class JobAlertController {

    private final JobAlertService jobAlertService;
    private final CurrentUserService currentUserService;

    public JobAlertController(
            JobAlertService jobAlertService,
            CurrentUserService currentUserService) {
        this.jobAlertService = jobAlertService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<List<JobAlertDTO>> getMyAlerts() {
        return ResponseEntity.ok(jobAlertService.getMyAlerts(currentUserService.getUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobAlertDTO> getAlert(@PathVariable Integer id) {
        return ResponseEntity.ok(jobAlertService.getAlert(currentUserService.getUser(), id));
    }

    @PostMapping
    public ResponseEntity<JobAlertDTO> createAlert(@RequestBody JobAlertDTO request) {
        return ResponseEntity.status(201).body(
                jobAlertService.createAlert(currentUserService.getUser(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobAlertDTO> updateAlert(
            @PathVariable Integer id,
            @RequestBody JobAlertDTO request) {
        return ResponseEntity.ok(
                jobAlertService.updateAlert(currentUserService.getUser(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Integer id) {
        jobAlertService.deleteAlert(currentUserService.getUser(), id);
        return ResponseEntity.noContent().build();
    }
}
