package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.CandidateSkillResponse;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.CandidateSkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidate-skills")
public class CandidateSkillController {

    private final CandidateSkillService candidateSkillService;
    private final CurrentUserService currentUserService;

    public CandidateSkillController(
            CandidateSkillService candidateSkillService,
            CurrentUserService currentUserService) {
        this.candidateSkillService = candidateSkillService;
        this.currentUserService = currentUserService;
    }

    /** All available skills. */
    @GetMapping
    public ResponseEntity<List<CandidateSkillResponse>> getAllSkills() {
        return ResponseEntity.ok(candidateSkillService.getAllSkills());
    }

    /** Skills on the logged-in candidate's profile. */
    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<CandidateSkillResponse>> getCandidateSkills() {
        return ResponseEntity.ok(
                candidateSkillService.getCandidateSkills(currentUserService.getUser()));
    }

    @PostMapping("/me/{skillId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateSkillResponse> addSkill(@PathVariable Integer skillId) {
        return ResponseEntity.ok(
                candidateSkillService.addSkill(currentUserService.getUser(), skillId));
    }

    @DeleteMapping("/me/{skillId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> removeSkill(@PathVariable Integer skillId) {
        candidateSkillService.removeSkill(currentUserService.getUser(), skillId);
        return ResponseEntity.noContent().build();
    }
}
