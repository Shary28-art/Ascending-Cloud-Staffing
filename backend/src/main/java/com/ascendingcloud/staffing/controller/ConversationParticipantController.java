package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.ConversationParticipant;
import com.ascendingcloud.staffing.service.ConversationParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversation-participants")
@CrossOrigin(origins = "*")
public class ConversationParticipantController {

    private final ConversationParticipantService service;

    public ConversationParticipantController(ConversationParticipantService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ConversationParticipant>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationParticipant> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ConversationParticipant> create(
            @RequestBody ConversationParticipant participant) {
        return ResponseEntity.ok(service.create(participant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}