package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public record ConversationDTO(
        Integer id,
        LocalDateTime createdAt
) {}