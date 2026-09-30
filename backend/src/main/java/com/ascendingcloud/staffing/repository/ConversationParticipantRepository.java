package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationParticipantRepository
        extends JpaRepository<ConversationParticipant, Integer> {
}