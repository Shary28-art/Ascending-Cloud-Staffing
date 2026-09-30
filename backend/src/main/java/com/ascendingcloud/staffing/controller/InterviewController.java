package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.Interview;
import com.ascendingcloud.staffing.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin(origins = "*")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping
    public ResponseEntity<List<Interview>> getAllInterviews() {
        return ResponseEntity.ok(interviewService.getAllInterviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interview> getInterviewById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                interviewService.getInterviewById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Interview> createInterview(
            @RequestBody Interview interview) {
        return ResponseEntity.ok(
                interviewService.createInterview(interview)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interview> updateInterview(
            @PathVariable Integer id,
            @RequestBody Interview interview) {
        return ResponseEntity.ok(
                interviewService.updateInterview(id, interview)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(
            @PathVariable Integer id) {
        interviewService.deleteInterview(id);
        return ResponseEntity.noContent().build();
    }
}