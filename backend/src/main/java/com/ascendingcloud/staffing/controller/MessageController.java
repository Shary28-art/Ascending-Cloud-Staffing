package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.MessageDTO;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final CurrentUserService currentUserService;

    public MessageController(
            MessageService messageService,
            CurrentUserService currentUserService) {
        this.messageService = messageService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByConversation(
            @PathVariable Integer conversationId) {
        return ResponseEntity.ok(
                messageService.getMessages(currentUserService.getUser(), conversationId));
    }

    /** Body: {"conversationId": 3, "content": "Hello"}. */
    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageDTO request) {
        return ResponseEntity.status(201).body(
                messageService.sendMessage(currentUserService.getUser(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer id) {
        User user = currentUserService.getUser();
        messageService.deleteMessage(user, id, currentUserService.isAdmin(user));
        return ResponseEntity.noContent().build();
    }
}
