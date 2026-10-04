package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Conversation;
import com.ascendingcloud.staffing.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    public Conversation getConversationById(Integer id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
    }

    public Conversation createConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    public Conversation updateConversation(
            Integer id,
            Conversation conversation) {

        Conversation existingConversation = getConversationById(id);

        return conversationRepository.save(existingConversation);
    }

    public void deleteConversation(Integer id) {
        if (!conversationRepository.existsById(id)) {
            throw new RuntimeException("Conversation not found");
        }

        conversationRepository.deleteById(id);
    }
}