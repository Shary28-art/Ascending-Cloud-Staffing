package com.ascendingcloud.staffing.dto;

public record LoginRequest(
        String email,
        String password
) {
}
