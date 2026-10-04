package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.ConversationParticipant;
import com.ascendingcloud.staffing.entity.ConversationParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationParticipantRepository
        extends JpaRepository<ConversationParticipant, ConversationParticipantId> {

    List<ConversationParticipant> findByConversationId(Integer conversationId);

    List<ConversationParticipant> findByUserId(Integer userId);

    boolean existsByConversationIdAndUserId(
            Integer conversationId,
            Integer userId
    );
}