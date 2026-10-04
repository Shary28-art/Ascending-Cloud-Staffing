package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.ConversationParticipantDTO;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.ConversationParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversation-participants")
public class ConversationParticipantController {

    private final ConversationParticipantService service;
    private final CurrentUserService currentUserService;

    public ConversationParticipantController(
            ConversationParticipantService service,
            CurrentUserService currentUserService) {
        this.service = service;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<ConversationParticipantDTO>> getByConversation(
            @PathVariable Integer conversationId) {
        return ResponseEntity.ok(
                service.getByConversation(currentUserService.getUser(), conversationId));
    }

    /** Body: {"conversationId": 3, "userId": 7}. */
    @PostMapping
    public ResponseEntity<ConversationParticipantDTO> addParticipant(
            @RequestBody ConversationParticipantDTO request) {
        return ResponseEntity.status(201).body(service.addParticipant(
                currentUserService.getUser(), request.conversationId(), request.userId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Integer id) {
        service.remove(currentUserService.getUser(), id);
        return ResponseEntity.noContent().build();
    }
}
