package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
}