package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.MessageDTO;
import com.ascendingcloud.staffing.entity.Conversation;
import com.ascendingcloud.staffing.entity.Message;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.ConversationParticipantRepository;
import com.ascendingcloud.staffing.repository.MessageRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;
    private final ConversationParticipantRepository participantRepository;
    private final NotificationService notificationService;

    public MessageService(
            MessageRepository messageRepository,
            ConversationService conversationService,
            ConversationParticipantRepository participantRepository,
            NotificationService notificationService) {
        this.messageRepository = messageRepository;
        this.conversationService = conversationService;
        this.participantRepository = participantRepository;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> getMessages(User user, Integer conversationId) {
        conversationService.assertParticipant(user, conversationId);

        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /** Sender is always the logged-in user. Only participants can post. */
    @Transactional
    public MessageDTO sendMessage(User user, MessageDTO dto) {
        if (dto == null || dto.conversationId() == null) {
            throw new BadRequestException("conversationId is required");
        }

        if (dto.content() == null || dto.content().isBlank()) {
            throw new BadRequestException("Message content is required");
        }

        // Posting requires real membership (admin read access is not enough)
        if (!participantRepository.existsByConversationIdAndUserId(
                dto.conversationId(), user.getId())) {
            throw new AccessDeniedException("You are not a participant in this conversation");
        }

        Conversation conversation = conversationService.find(dto.conversationId());

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(user);
        message.setContent(dto.content().trim());

        Message saved = messageRepository.save(message);

        participantRepository.findByConversationId(conversation.getId())
                .stream()
                .filter(p -> !p.getUser().getId().equals(user.getId()))
                .forEach(p -> notificationService.notify(p.getUser(),
                        "New message", "You have a new message from " + user.getName() + "."));

        return toDTO(saved);
    }

    @Transactional
    public void deleteMessage(User user, Integer id, boolean isAdmin) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        if (!isAdmin && !message.getSender().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only delete your own messages");
        }

        messageRepository.delete(message);
    }

    private MessageDTO toDTO(Message m) {
        return new MessageDTO(
                m.getId(),
                m.getConversation().getId(),
                m.getSender().getId(),
                m.getSender().getName(),
                m.getContent(),
                m.getSentAt());
    }
}
