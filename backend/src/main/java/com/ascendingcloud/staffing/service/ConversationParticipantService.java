package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.ConversationParticipantDTO;
import com.ascendingcloud.staffing.entity.Conversation;
import com.ascendingcloud.staffing.entity.ConversationParticipant;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.ConversationParticipantRepository;
import com.ascendingcloud.staffing.repository.UserRepository;
import com.ascendingcloud.staffing.security.CurrentUserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConversationParticipantService {

    private final ConversationParticipantRepository repository;
    private final ConversationService conversationService;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public ConversationParticipantService(
            ConversationParticipantRepository repository,
            ConversationService conversationService,
            UserRepository userRepository,
            CurrentUserService currentUserService) {
        this.repository = repository;
        this.conversationService = conversationService;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public List<ConversationParticipantDTO> getByConversation(User user, Integer conversationId) {
        conversationService.assertParticipant(user, conversationId);

        return repository.findByConversationId(conversationId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public ConversationParticipantDTO addParticipant(
            User user, Integer conversationId, Integer userId) {

        conversationService.assertParticipant(user, conversationId);

        Conversation conversation = conversationService.find(conversationId);

        User newUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (repository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new BadRequestException("User is already in this conversation");
        }

        return toDTO(repository.save(new ConversationParticipant(conversation, newUser)));
    }

    /** A user can leave a conversation; admins can remove anyone. */
    @Transactional
    public void remove(User user, Integer participantId) {
        ConversationParticipant participant = repository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found"));

        boolean self = participant.getUser().getId().equals(user.getId());

        if (!self && !currentUserService.isAdmin(user)) {
            throw new AccessDeniedException("You cannot remove another participant");
        }

        repository.delete(participant);
    }

    private ConversationParticipantDTO toDTO(ConversationParticipant p) {
        return new ConversationParticipantDTO(
                p.getId(),
                p.getConversation().getId(),
                p.getUser().getId(),
                p.getUser().getName());
    }
}
