package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public record SavedJobDTO(
        Integer id,
        Integer jobId,
        LocalDateTime savedAt,
        JobDTO job
) {
}
