package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public record UserAdminRoleDTO(
        Integer id,
        Integer userId,
        Integer adminRoleId,
        LocalDateTime assignedAt
) {
}
