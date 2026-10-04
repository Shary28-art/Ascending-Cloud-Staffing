package com.ascendingcloud.staffing.dto;

/** Body for status changes, e.g. {"status": "Shortlisted"}. */
public record StatusUpdateRequest(String status) {
}
