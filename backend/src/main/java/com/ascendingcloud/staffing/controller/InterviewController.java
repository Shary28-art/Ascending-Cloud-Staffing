package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.InterviewDTO;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;
    private final CurrentUserService currentUserService;

    public InterviewController(
            InterviewService interviewService,
            CurrentUserService currentUserService) {
        this.interviewService = interviewService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<List<InterviewDTO>> getInterviews() {
        return ResponseEntity.ok(interviewService.getInterviews(currentUserService.getUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewDTO> getInterview(@PathVariable Integer id) {
        return ResponseEntity.ok(
                interviewService.getInterview(currentUserService.getUser(), id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<InterviewDTO> createInterview(@RequestBody InterviewDTO request) {
        return ResponseEntity.status(201).body(
                interviewService.createInterview(currentUserService.getUser(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<InterviewDTO> updateInterview(
            @PathVariable Integer id,
            @RequestBody InterviewDTO request) {
        return ResponseEntity.ok(
                interviewService.updateInterview(currentUserService.getUser(), id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<Void> deleteInterview(@PathVariable Integer id) {
        interviewService.deleteInterview(currentUserService.getUser(), id);
        return ResponseEntity.noContent().build();
    }
}
