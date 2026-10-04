package com.ascendingcloud.staffing.dto;

public record ConversationParticipantDTO(
        Integer id,
        Integer conversationId,
        Integer userId,
        String userName
) {
}
