package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public record InterviewDTO(
        Integer id,
        Integer applicationId,
        LocalDateTime scheduledAt,
        String interviewType,
        String status,
        String meetingLink,
        String notes
) {
}
