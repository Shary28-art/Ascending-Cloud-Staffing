package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.Resume;
import com.ascendingcloud.staffing.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = "*")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping
    public ResponseEntity<List<Resume>> getAllResumes() {
        return ResponseEntity.ok(resumeService.getAllResumes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResumeById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                resumeService.getResumeById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Resume> createResume(
            @RequestBody Resume resume) {
        return ResponseEntity.ok(
                resumeService.createResume(resume)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resume> updateResume(
            @PathVariable Integer id,
            @RequestBody Resume resume) {
        return ResponseEntity.ok(
                resumeService.updateResume(id, resume)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(
            @PathVariable Integer id) {
        resumeService.deleteResume(id);
        return ResponseEntity.noContent().build();
    }
}