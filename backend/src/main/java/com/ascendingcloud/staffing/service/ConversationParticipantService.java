package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.ConversationParticipant;
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

    public ConversationParticipant getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Conversation participant not found"));
    }

    public ConversationParticipant create(
            ConversationParticipant participant) {
        return repository.save(participant);
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException(
                    "Conversation participant not found");
        }

        repository.deleteById(id);
    }
}