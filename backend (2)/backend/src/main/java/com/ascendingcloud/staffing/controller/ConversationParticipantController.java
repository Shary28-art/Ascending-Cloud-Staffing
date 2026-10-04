package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.ConversationParticipant;
import com.ascendingcloud.staffing.service.ConversationParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversation-participants")
public class ConversationParticipantController {

    private final ConversationParticipantService service;

    public ConversationParticipantController(
            ConversationParticipantService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ConversationParticipant>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{conversationId}/{userId}")
    public ResponseEntity<ConversationParticipant> getById(
            @PathVariable Integer conversationId,
            @PathVariable Integer userId) {

        return ResponseEntity.ok(
                service.getById(conversationId, userId)
        );
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<ConversationParticipant>> getByConversationId(
            @PathVariable Integer conversationId) {

        return ResponseEntity.ok(
                service.getByConversationId(conversationId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConversationParticipant>> getByUserId(
            @PathVariable Integer userId) {

        return ResponseEntity.ok(
                service.getByUserId(userId)
        );
    }

    @PostMapping
    public ResponseEntity<ConversationParticipant> create(
            @RequestBody ConversationParticipant participant) {

        return ResponseEntity.ok(
                service.create(participant)
        );
    }

    @DeleteMapping("/{conversationId}/{userId}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer conversationId,
            @PathVariable Integer userId) {

        service.delete(conversationId, userId);
        return ResponseEntity.noContent().build();
    }
}