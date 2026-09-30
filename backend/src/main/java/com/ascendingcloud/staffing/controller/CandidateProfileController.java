package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.CandidateProfileResponse;
import com.ascendingcloud.staffing.service.CandidateProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/candidate-profiles")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    public CandidateProfileController(
            CandidateProfileService candidateProfileService) {

        this.candidateProfileService = candidateProfileService;
    }

    @PostMapping
    public ResponseEntity<CandidateProfileResponse> createProfile(
            @RequestParam String email,
            @RequestBody CandidateProfileRequest request) {

        CandidateProfileResponse profile =
                candidateProfileService.createProfile(
                        email,
                        request.phone(),
                        request.location(),
                        request.bio(),
                        request.professionalHeadline(),
                        request.professionalSummary(),
                        request.currentJobTitle(),
                        request.totalYearsExperience(),
                        request.desiredJobTitle(),
                        request.preferredEmploymentType(),
                        request.preferredWorkArrangement(),
                        request.preferredJobLocation(),
                        request.salaryExpectation(),
                        request.availabilityDate(),
                        request.workAuthorizationStatus(),
                        request.sponsorshipRequired(),
                        request.securityClearanceStatus(),
                        request.profileVisibility()
                );

        return ResponseEntity.ok(profile);
    }

    @GetMapping("/me")
    public ResponseEntity<CandidateProfileResponse> getProfile(
            @RequestParam String email) {

        return ResponseEntity.ok(
                candidateProfileService.getProfile(email)
        );
    }

    @PutMapping("/me")
    public ResponseEntity<CandidateProfileResponse> updateProfile(
            @RequestParam String email,
            @RequestBody CandidateProfileRequest request) {

        return ResponseEntity.ok(
                candidateProfileService.updateProfile(
                        email,
                        request.phone(),
                        request.location(),
                        request.bio(),
                        request.professionalHeadline(),
                        request.professionalSummary(),
                        request.currentJobTitle(),
                        request.totalYearsExperience(),
                        request.desiredJobTitle(),
                        request.preferredEmploymentType(),
                        request.preferredWorkArrangement(),
                        request.preferredJobLocation(),
                        request.salaryExpectation(),
                        request.availabilityDate(),
                        request.workAuthorizationStatus(),
                        request.sponsorshipRequired(),
                        request.securityClearanceStatus(),
                        request.profileVisibility()
                )
        );
    }

    public record CandidateProfileRequest(
            String phone,
            String location,
            String bio,
            String professionalHeadline,
            String professionalSummary,
            String currentJobTitle,
            BigDecimal totalYearsExperience,
            String desiredJobTitle,
            String preferredEmploymentType,
            String preferredWorkArrangement,
            String preferredJobLocation,
            String salaryExpectation,
            LocalDate availabilityDate,
            String workAuthorizationStatus,
            Boolean sponsorshipRequired,
            String securityClearanceStatus,
            Boolean profileVisibility
    ) {
    }
}