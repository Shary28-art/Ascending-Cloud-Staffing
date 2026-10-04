package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.Skill;
import com.ascendingcloud.staffing.service.JobSkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-skills")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class JobSkillController {

    private final JobSkillService jobSkillService;

    public JobSkillController(JobSkillService jobSkillService) {
        this.jobSkillService = jobSkillService;
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<List<Skill>> getJobSkills(
            @PathVariable Integer jobId) {

        return ResponseEntity.ok(
                jobSkillService.getJobSkills(jobId)
        );
    }

    @PostMapping
    public ResponseEntity<Skill> addSkill(
            @RequestParam Integer jobId,
            @RequestParam Integer skillId) {

        return ResponseEntity.ok(
                jobSkillService.addSkill(
                        jobId,
                        skillId
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> removeSkill(
            @RequestParam Integer jobId,
            @RequestParam Integer skillId) {

        jobSkillService.removeSkill(
                jobId,
                skillId
        );

        return ResponseEntity.noContent().build();
    }
}