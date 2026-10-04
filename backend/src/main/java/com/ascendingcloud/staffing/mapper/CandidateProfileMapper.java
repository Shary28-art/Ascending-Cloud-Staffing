package com.ascendingcloud.staffing.mapper;

import com.ascendingcloud.staffing.dto.CandidateProfileResponse;
import com.ascendingcloud.staffing.entity.CandidateProfile;

public class CandidateProfileMapper {

    private CandidateProfileMapper() {
    }

    public static CandidateProfileResponse toResponse(CandidateProfile profile) {

        if (profile == null) {
            return null;
        }

        return new CandidateProfileResponse(
                profile.getId(),
                profile.getUser() != null ? profile.getUser().getName() : null,
                profile.getUser() != null ? profile.getUser().getEmail() : null,
                profile.getPhone(),
                profile.getLocation(),
                profile.getCity(),
                profile.getState(),
                profile.getCountry(),
                profile.getBio(),
                profile.getProfessionalHeadline(),
                profile.getProfessionalSummary(),
                profile.getCurrentJobTitle(),
                profile.getTotalYearsExperience(),
                profile.getDesiredJobTitle(),
                profile.getPreferredEmploymentType(),
                profile.getPreferredWorkArrangement(),
                profile.getPreferredJobLocation(),
                profile.getSalaryExpectation(),
                profile.getAvailabilityDate(),
                profile.getWorkAuthorizationStatus(),
                profile.getSponsorshipRequired(),
                profile.getSecurityClearanceStatus(),
                profile.getLinkedinPortfolioUrl(),
                profile.getProfileVisibility(),
                profile.getShareWithEmployers()
        );
    }
}
