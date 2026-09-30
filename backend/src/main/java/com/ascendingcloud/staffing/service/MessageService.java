package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Message;
import com.ascendingcloud.staffing.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    public List<Message> getMessagesByConversationId(Integer conversationId) {
        return messageRepository
                .findByConversationIdOrderBySentAtAsc(conversationId);
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message updateMessage(Integer id, Message message) {
        Message existingMessage = getMessageById(id);

        existingMessage.setConversation(message.getConversation());
        existingMessage.setSender(message.getSender());
        existingMessage.setContent(message.getContent());
        existingMessage.setSentAt(message.getSentAt());

        return messageRepository.save(existingMessage);
    }

    public void deleteMessage(Integer id) {
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("Message not found");
        }

        messageRepository.deleteById(id);
    }
}