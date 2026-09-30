package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.CandidateSkillResponse;
import com.ascendingcloud.staffing.service.CandidateSkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidate-skills")
public class CandidateSkillController {

    private final CandidateSkillService candidateSkillService;

    public CandidateSkillController(CandidateSkillService candidateSkillService) {
        this.candidateSkillService = candidateSkillService;
    }

    // Get all available skills
    @GetMapping
    public ResponseEntity<List<CandidateSkillResponse>> getAllSkills() {
        return ResponseEntity.ok(candidateSkillService.getAllSkills());
    }

    // Get skills assigned to a candidate
    @GetMapping("/me")
    public ResponseEntity<List<CandidateSkillResponse>> getCandidateSkills(
            @RequestParam String email
    ) {
        return ResponseEntity.ok(
                candidateSkillService.getCandidateSkills(email)
        );
    }

    // Add a skill to candidate profile
    @PostMapping("/me/{skillId}")
    public ResponseEntity<CandidateSkillResponse> addSkill(
            @RequestParam String email,
            @PathVariable Integer skillId
    ) {
        return ResponseEntity.ok(
                candidateSkillService.addSkill(email, skillId)
        );
    }

    // Remove a skill from candidate profile
    @DeleteMapping("/me/{skillId}")
    public ResponseEntity<Void> removeSkill(
            @RequestParam String email,
            @PathVariable Integer skillId
    ) {
        candidateSkillService.removeSkill(email, skillId);
        return ResponseEntity.noContent().build();
    }
}