package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public record MessageDTO(
        Integer id,
        Integer conversationId,
        Integer senderId,
        String content,
        LocalDateTime sentAt
) {}