package com.ascendingcloud.staffing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CandidateProfileResponse(
        Integer id,
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