package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

/** For requests only conversationId and content are used (sender comes from the login). */
public record MessageDTO(
        Integer id,
        Integer conversationId,
        Integer senderId,
        String senderName,
        String content,
        LocalDateTime sentAt
) {
}
