package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.JobAlert;
import com.ascendingcloud.staffing.service.JobAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-alerts")
@CrossOrigin(origins = "*")
public class JobAlertController {

    private final JobAlertService jobAlertService;

    public JobAlertController(JobAlertService jobAlertService) {
        this.jobAlertService = jobAlertService;
    }

    @GetMapping
    public ResponseEntity<List<JobAlert>> getAllAlerts() {
        return ResponseEntity.ok(jobAlertService.getAllAlerts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobAlert> getAlertById(@PathVariable Integer id) {
        return ResponseEntity.ok(jobAlertService.getAlertById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JobAlert>> getAlertsByUser(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(jobAlertService.getAlertsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<JobAlert> createAlert(
            @RequestBody JobAlert jobAlert) {
        return ResponseEntity.ok(jobAlertService.createAlert(jobAlert));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobAlert> updateAlert(
            @PathVariable Integer id,
            @RequestBody JobAlert jobAlert) {
        return ResponseEntity.ok(
                jobAlertService.updateAlert(id, jobAlert)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(
            @PathVariable Integer id) {
        jobAlertService.deleteAlert(id);
        return ResponseEntity.noContent().build();
    }
}