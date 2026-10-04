package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;
import java.util.List;

/** For requests only participantUserIds is used (the caller is added automatically). */
public record ConversationDTO(
        Integer id,
        LocalDateTime createdAt,
        List<Integer> participantUserIds
) {
}
