package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.ConversationDTO;
import com.ascendingcloud.staffing.entity.Conversation;
import com.ascendingcloud.staffing.entity.ConversationParticipant;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.ConversationParticipantRepository;
import com.ascendingcloud.staffing.repository.ConversationRepository;
import com.ascendingcloud.staffing.repository.UserRepository;
import com.ascendingcloud.staffing.security.CurrentUserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public ConversationService(
            ConversationRepository conversationRepository,
            ConversationParticipantRepository participantRepository,
            UserRepository userRepository,
            CurrentUserService currentUserService) {
        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public List<ConversationDTO> getMyConversations(User user) {
        return participantRepository.findByUserId(user.getId())
                .stream()
                .map(p -> toDTO(p.getConversation()))
                .toList();
    }

    @Transactional(readOnly = true)
    public ConversationDTO getConversation(User user, Integer id) {
        Conversation conversation = find(id);
        assertParticipant(user, id);

        return toDTO(conversation);
    }

    /** The caller is added automatically; dto.participantUserIds lists the others. */
    @Transactional
    public ConversationDTO createConversation(User user, ConversationDTO dto) {
        Set<Integer> userIds = new LinkedHashSet<>();
        userIds.add(user.getId());

        if (dto != null && dto.participantUserIds() != null) {
            userIds.addAll(dto.participantUserIds());
        }

        if (userIds.size() < 2) {
            throw new BadRequestException("A conversation needs at least one other participant");
        }

        Conversation conversation = conversationRepository.save(new Conversation());

        for (Integer userId : userIds) {
            User participant = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User not found with id: " + userId));

            participantRepository.save(new ConversationParticipant(conversation, participant));
        }

        return toDTO(conversation);
    }

    @Transactional
    public void deleteConversation(Integer id) {
        if (!conversationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Conversation not found");
        }

        conversationRepository.deleteById(id);
    }

    public Conversation find(Integer id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
    }

    /** Admins may read any conversation (moderation); everyone else must be a participant. */
    public void assertParticipant(User user, Integer conversationId) {
        if (currentUserService.isAdmin(user)) {
            return;
        }

        if (!participantRepository.existsByConversationIdAndUserId(conversationId, user.getId())) {
            throw new AccessDeniedException("You are not a participant in this conversation");
        }
    }

    private ConversationDTO toDTO(Conversation c) {
        List<Integer> ids = participantRepository.findByConversationId(c.getId())
                .stream()
                .map(p -> p.getUser().getId())
                .toList();

        return new ConversationDTO(c.getId(), c.getCreatedAt(), ids);
    }
}
