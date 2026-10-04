package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public record JobAlertDTO(
        Integer id,
        Integer userId,
        String keyword,
        String location,
        Integer jobCategoryId,
        Integer employmentTypeId,
        Integer experienceLevelId,
        LocalDateTime createdAt
) {}