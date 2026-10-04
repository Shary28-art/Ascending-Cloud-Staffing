package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public record NotificationDTO(
        Integer id,
        Integer userId,
        String title,
        String message,
        Boolean isRead,
        LocalDateTime createdAt
) {}