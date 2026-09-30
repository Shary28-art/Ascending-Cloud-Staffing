package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.Conversation;
import com.ascendingcloud.staffing.service.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@CrossOrigin(origins = "*")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping
    public ResponseEntity<List<Conversation>> getAllConversations() {
        return ResponseEntity.ok(conversationService.getAllConversations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conversation> getConversationById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                conversationService.getConversationById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Conversation> createConversation(
            @RequestBody Conversation conversation) {
        return ResponseEntity.ok(
                conversationService.createConversation(conversation)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conversation> updateConversation(
            @PathVariable Integer id,
            @RequestBody Conversation conversation) {
        return ResponseEntity.ok(
                conversationService.updateConversation(id, conversation)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversation(
            @PathVariable Integer id) {
        conversationService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }
}