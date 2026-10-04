package com.ascendingcloud.staffing.mapper;

import com.ascendingcloud.staffing.dto.JobDTO;
import com.ascendingcloud.staffing.entity.Job;

public class JobMapper {

    private JobMapper() {
    }

    public static JobDTO toDTO(Job job) {
        if (job == null) {
            return null;
        }

        return new JobDTO(
                job.getId(),
                job.getTitle(),
                job.getCompany() != null ? job.getCompany().getId() : null,
                job.getCompany() != null ? job.getCompany().getName() : null,
                job.getLocation(),
                job.getSalary(),
                job.getCloudPlatform() != null ? job.getCloudPlatform().getId() : null,
                job.getEmploymentType() != null ? job.getEmploymentType().getId() : null,
                job.getExperienceLevel() != null ? job.getExperienceLevel().getId() : null,
                job.getWorkplaceType() != null ? job.getWorkplaceType().getId() : null,
                job.getJobCategory() != null ? job.getJobCategory().getId() : null,
                job.getDescription(),
                job.getResponsibilities(),
                job.getRequiredQualifications(),
                job.getPreferredSkills(),
                job.getEducationRequirements(),
                job.getCertificationRequirements(),
                job.getNumberOfPositions(),
                job.getApplicationDeadline(),
                job.getExpectedStartDate(),
                job.getWorkAuthorization(),
                job.getSponsorshipAvailable(),
                job.getSecurityClearanceRequired(),
                job.getTravelRequirements(),
                job.getScreeningQuestions(),
                job.getAssignedRecruiterId(),
                job.getStatus(),
                job.getApprovalStatus(),
                job.getCreatedAt()
        );
    }
}
