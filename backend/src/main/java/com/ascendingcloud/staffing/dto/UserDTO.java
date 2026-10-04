package com.ascendingcloud.staffing.dto;

public record UserDTO(
        Integer id,
        String name,
        String email,
        String role
) {
}
