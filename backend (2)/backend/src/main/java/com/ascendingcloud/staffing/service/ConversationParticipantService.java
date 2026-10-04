package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.ConversationParticipant;
import com.ascendingcloud.staffing.entity.ConversationParticipantId;
import com.ascendingcloud.staffing.repository.ConversationParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationParticipantService {

    private final ConversationParticipantRepository repository;

    public ConversationParticipantService(
            ConversationParticipantRepository repository) {
        this.repository = repository;
    }

    public List<ConversationParticipant> getAll() {
        return repository.findAll();
    }

    public ConversationParticipant getById(
            Integer conversationId,
            Integer userId) {

        ConversationParticipantId id =
                new ConversationParticipantId(
                        conversationId,
                        userId
                );

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Conversation participant not found"));
    }

    public List<ConversationParticipant> getByConversationId(
            Integer conversationId) {

        return repository.findByConversationId(conversationId);
    }

    public List<ConversationParticipant> getByUserId(
            Integer userId) {

        return repository.findByUserId(userId);
    }

    public ConversationParticipant create(
            ConversationParticipant participant) {

        return repository.save(participant);
    }

    public void delete(
            Integer conversationId,
            Integer userId) {

        ConversationParticipantId id =
                new ConversationParticipantId(
                        conversationId,
                        userId
                );

        if (!repository.existsById(id)) {
            throw new RuntimeException(
                    "Conversation participant not found");
        }

        repository.deleteById(id);
    }
}