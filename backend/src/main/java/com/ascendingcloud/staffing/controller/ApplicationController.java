package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.Application;
import com.ascendingcloud.staffing.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                applicationService.getApplicationById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Application> createApplication(
            @RequestBody Application application) {
        return ResponseEntity.ok(
                applicationService.createApplication(application)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Application> updateApplication(
            @PathVariable Integer id,
            @RequestBody Application application) {
        return ResponseEntity.ok(
                applicationService.updateApplication(id, application)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Integer id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}