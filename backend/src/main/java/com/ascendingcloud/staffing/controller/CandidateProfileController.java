package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.CandidateProfileRequest;
import com.ascendingcloud.staffing.dto.CandidateProfileResponse;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.CandidateProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidate-profiles")
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;
    private final CurrentUserService currentUserService;

    public CandidateProfileController(
            CandidateProfileService candidateProfileService,
            CurrentUserService currentUserService) {
        this.candidateProfileService = candidateProfileService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileResponse> createProfile(
            @RequestBody CandidateProfileRequest request) {
        return ResponseEntity.status(201).body(
                candidateProfileService.createProfile(currentUserService.getUser(), request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileResponse> getMyProfile() {
        return ResponseEntity.ok(
                candidateProfileService.getMyProfile(currentUserService.getUser()));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileResponse> updateMyProfile(
            @RequestBody CandidateProfileRequest request) {
        return ResponseEntity.ok(
                candidateProfileService.updateMyProfile(currentUserService.getUser(), request));
    }

    /** For employers (shared or applicant profiles) and admins. */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<CandidateProfileResponse> getCandidateProfile(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                candidateProfileService.getProfileForViewer(currentUserService.getUser(), id));
    }
}
