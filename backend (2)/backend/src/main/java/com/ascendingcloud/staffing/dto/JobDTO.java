package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public record JobDTO(
        Integer id,
        String title,
        Integer companyId,
        String location,
        String salary,
        Integer cloudPlatformId,
        Integer employmentTypeId,
        Integer experienceLevelId,
        Integer workplaceTypeId,
        String description,
        LocalDateTime createdAt
) {}