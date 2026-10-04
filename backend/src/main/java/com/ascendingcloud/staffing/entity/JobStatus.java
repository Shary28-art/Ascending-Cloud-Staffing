package com.ascendingcloud.staffing.entity;

import com.ascendingcloud.staffing.exception.BadRequestException;

/** Job lifecycle status: draft, active, paused, filled, closed or expired. */
public enum JobStatus {
    DRAFT,
    ACTIVE,
    PAUSED,
    FILLED,
    CLOSED,
    EXPIRED;

    public static JobStatus parse(String value) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException("Job status is required");
        }

        try {
            return JobStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid job status: " + value);
        }
    }
}
