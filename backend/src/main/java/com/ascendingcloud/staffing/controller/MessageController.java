package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.Message;
import com.ascendingcloud.staffing.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer id) {
        return ResponseEntity.ok(messageService.getMessageById(id));
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<Message>> getMessagesByConversation(
            @PathVariable Integer conversationId) {
        return ResponseEntity.ok(
                messageService.getMessagesByConversationId(conversationId)
        );
    }

    @PostMapping
    public ResponseEntity<Message> createMessage(
            @RequestBody Message message) {
        return ResponseEntity.ok(messageService.createMessage(message));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(
            @PathVariable Integer id,
            @RequestBody Message message) {
        return ResponseEntity.ok(
                messageService.updateMessage(id, message)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}