package com.ascendingcloud.staffing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CandidateProfileResponse(
        Integer id,
        String name,
        String email,
        String phone,
        String location,
        String city,
        String state,
        String country,
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
        String linkedinPortfolioUrl,
        Boolean profileVisibility,
        Boolean shareWithEmployers
) {
}
