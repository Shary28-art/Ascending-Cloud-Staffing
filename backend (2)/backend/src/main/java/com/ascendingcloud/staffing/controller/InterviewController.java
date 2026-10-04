package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.Interview;
import com.ascendingcloud.staffing.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interviews")
@CrossOrigin(origins = "*")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping
    public ResponseEntity<List<Interview>> getAllInterviews() {
        return ResponseEntity.ok(
                interviewService.getAllInterviews()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interview> getInterviewById(
            @PathVariable Integer id) {

        Interview interview =
                interviewService.getInterviewById(id);

        if (interview == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(interview);
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

        Interview updated =
                interviewService.updateInterview(id, interview);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(
            @PathVariable Integer id) {

        if (!interviewService.deleteInterview(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}