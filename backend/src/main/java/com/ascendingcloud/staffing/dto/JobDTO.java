package com.ascendingcloud.staffing.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Flat job shape. Used as the request body for create/update and as the
 * read model for saved jobs. The server controls id, companyName,
 * approvalStatus and createdAt, so they are ignored on input.
 */
public record JobDTO(
        Integer id,
        String title,
        Integer companyId,
        String companyName,
        String location,
        String salary,
        Integer cloudPlatformId,
        Integer employmentTypeId,
        Integer experienceLevelId,
        Integer workplaceTypeId,
        Integer jobCategoryId,
        String description,
        String responsibilities,
        String requiredQualifications,
        String preferredSkills,
        String educationRequirements,
        String certificationRequirements,
        Integer numberOfPositions,
        LocalDate applicationDeadline,
        LocalDate expectedStartDate,
        String workAuthorization,
        Boolean sponsorshipAvailable,
        String securityClearanceRequired,
        String travelRequirements,
        String screeningQuestions,
        Integer assignedRecruiterId,
        String status,
        String approvalStatus,
        LocalDateTime createdAt
) {
}
