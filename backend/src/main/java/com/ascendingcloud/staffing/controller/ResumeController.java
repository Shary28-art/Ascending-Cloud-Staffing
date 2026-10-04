package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.ResumeDTO;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.ResumeService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    private final CurrentUserService currentUserService;

    public ResumeController(
            ResumeService resumeService,
            CurrentUserService currentUserService) {
        this.resumeService = resumeService;
        this.currentUserService = currentUserService;
    }

    /** multipart/form-data with a "file" part. PDF, DOC or DOCX, max 5 MB. Replaces any existing resume. */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ResumeDTO> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(201).body(
                resumeService.upload(currentUserService.getUser(), file));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ResumeDTO> getMyResume() {
        return ResponseEntity.ok(resumeService.getMyResume(currentUserService.getUser()));
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> deleteMyResume() {
        resumeService.deleteMyResume(currentUserService.getUser());
        return ResponseEntity.noContent().build();
    }

    /** Resume metadata of a candidate (employers need an application from them). */
    @GetMapping("/candidate/{candidateId}")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<ResumeDTO> getForCandidate(@PathVariable Integer candidateId) {
        return ResponseEntity.ok(
                resumeService.getForCandidate(currentUserService.getUser(), candidateId));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> download(@PathVariable Integer id) {
        ResumeService.ResumeFile file =
                resumeService.download(currentUserService.getUser(), id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(file.fileName(), StandardCharsets.UTF_8)
                                .build().toString())
                .header(HttpHeaders.CACHE_CONTROL, "private, no-store")
                .body(new InputStreamResource(file.stream()));
    }
}
