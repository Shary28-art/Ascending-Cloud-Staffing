package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationParticipantRepository
        extends JpaRepository<ConversationParticipant, Integer> {

    List<ConversationParticipant> findByUserId(Integer userId);

    List<ConversationParticipant> findByConversationId(Integer conversationId);

    boolean existsByConversationIdAndUserId(Integer conversationId, Integer userId);
}
