package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.ApplicationResponse;
import com.ascendingcloud.staffing.dto.ApplicationStatusHistoryResponse;
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

    @PostMapping
    public ResponseEntity<ApplicationResponse> apply(
            @RequestParam String email,
            @RequestParam Integer jobId) {

        return ResponseEntity.ok(
                applicationService.apply(email, jobId)
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(
            @RequestParam String email) {

        return ResponseEntity.ok(
                applicationService.getMyApplications(email)
        );
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponse>> getJobApplications(
            @RequestParam String email,
            @PathVariable Integer jobId) {

        return ResponseEntity.ok(
                applicationService.getJobApplications(
                        email,
                        jobId
                )
        );
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @RequestParam String email,
            @PathVariable Integer applicationId,
            @RequestParam String status) {

        return ResponseEntity.ok(
                applicationService.updateStatus(
                        email,
                        applicationId,
                        status
                )
        );
    }

    @GetMapping("/{applicationId}/history")
    public ResponseEntity<List<ApplicationStatusHistoryResponse>>
    getStatusHistory(
            @PathVariable Integer applicationId) {

        return ResponseEntity.ok(
                applicationService.getStatusHistory(applicationId)
        );
    }
}