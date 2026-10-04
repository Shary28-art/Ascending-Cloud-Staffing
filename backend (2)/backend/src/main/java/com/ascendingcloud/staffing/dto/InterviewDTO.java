package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public record InterviewDTO(
        Integer id,
        Integer applicationId,
        LocalDateTime interviewDate,
        String interviewType,
        String meetingLink,
        String notes,
        LocalDateTime createdAt
) {}