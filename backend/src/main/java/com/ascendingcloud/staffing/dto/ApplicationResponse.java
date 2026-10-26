package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public record ApplicationResponse(
        Integer id,
        Integer jobId,
        String jobTitle,
        Integer candidateId,
        String candidateName,
        String status,
        LocalDateTime appliedAt
) {
}
