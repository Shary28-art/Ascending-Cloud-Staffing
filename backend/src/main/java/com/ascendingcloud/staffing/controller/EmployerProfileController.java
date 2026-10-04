package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.EmployerProfileDTO;
import com.ascendingcloud.staffing.entity.EmployerProfile;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.EmployerProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employer-profiles")
@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerProfileController {

    private final EmployerProfileService employerProfileService;
    private final CurrentUserService currentUserService;

    public EmployerProfileController(
            EmployerProfileService employerProfileService,
            CurrentUserService currentUserService) {
        this.employerProfileService = employerProfileService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<EmployerProfile> createProfile(
            @RequestBody EmployerProfileDTO request) {
        return ResponseEntity.status(201).body(
                employerProfileService.createProfile(currentUserService.getUser(), request));
    }

    @GetMapping("/me")
    public ResponseEntity<EmployerProfile> getProfile() {
        return ResponseEntity.ok(
                employerProfileService.getProfile(currentUserService.getUser()));
    }

    @PutMapping("/me")
    public ResponseEntity<EmployerProfile> updateProfile(
            @RequestBody EmployerProfileDTO request) {
        return ResponseEntity.ok(
                employerProfileService.updateProfile(currentUserService.getUser(), request));
    }
}
