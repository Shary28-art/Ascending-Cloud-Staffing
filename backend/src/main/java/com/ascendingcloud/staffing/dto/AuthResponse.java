package com.ascendingcloud.staffing.dto;

public record AuthResponse(
        String token,
        Integer id,
        String name,
        String email,
        String role
) {
}
