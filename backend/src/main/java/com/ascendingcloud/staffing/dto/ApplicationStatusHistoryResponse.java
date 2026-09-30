package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public record ApplicationStatusHistoryResponse(
        Integer id,
        Integer applicationId,
        String status,
        LocalDateTime changedAt
) {
}