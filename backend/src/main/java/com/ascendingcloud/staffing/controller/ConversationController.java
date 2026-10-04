package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.ConversationDTO;
import com.ascendingcloud.staffing.dto.MessageDTO;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.ConversationService;
import com.ascendingcloud.staffing.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;
    private final MessageService messageService;
    private final CurrentUserService currentUserService;

    public ConversationController(
            ConversationService conversationService,
            MessageService messageService,
            CurrentUserService currentUserService) {
        this.conversationService = conversationService;
        this.messageService = messageService;
        this.currentUserService = currentUserService;
    }

    /** Conversations the logged-in user takes part in. */
    @GetMapping
    public ResponseEntity<List<ConversationDTO>> getMyConversations() {
        return ResponseEntity.ok(
                conversationService.getMyConversations(currentUserService.getUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDTO> getConversation(@PathVariable Integer id) {
        return ResponseEntity.ok(
                conversationService.getConversation(currentUserService.getUser(), id));
    }

    /** Body: {"participantUserIds": [5, 9]}. The caller is added automatically. */
    @PostMapping
    public ResponseEntity<ConversationDTO> createConversation(
            @RequestBody ConversationDTO request) {
        return ResponseEntity.status(201).body(
                conversationService.createConversation(currentUserService.getUser(), request));
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Integer id) {
        return ResponseEntity.ok(messageService.getMessages(currentUserService.getUser(), id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteConversation(@PathVariable Integer id) {
        conversationService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }
}
