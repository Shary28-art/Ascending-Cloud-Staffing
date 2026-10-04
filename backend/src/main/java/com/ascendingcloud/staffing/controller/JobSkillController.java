package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.Skill;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.JobSkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-skills")
public class JobSkillController {

    private final JobSkillService jobSkillService;
    private final CurrentUserService currentUserService;

    public JobSkillController(
            JobSkillService jobSkillService,
            CurrentUserService currentUserService) {
        this.jobSkillService = jobSkillService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<List<Skill>> getJobSkills(@PathVariable Integer jobId) {
        return ResponseEntity.ok(jobSkillService.getJobSkills(jobId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<Skill> addSkill(
            @RequestParam Integer jobId,
            @RequestParam Integer skillId) {
        return ResponseEntity.ok(
                jobSkillService.addSkill(currentUserService.getUser(), jobId, skillId));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<Void> removeSkill(
            @RequestParam Integer jobId,
            @RequestParam Integer skillId) {
        jobSkillService.removeSkill(currentUserService.getUser(), jobId, skillId);
        return ResponseEntity.noContent().build();
    }
}
