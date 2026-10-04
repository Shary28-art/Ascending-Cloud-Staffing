package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.JobDTO;
import com.ascendingcloud.staffing.dto.RejectRequest;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Job approval queue. Everything under /api/admin requires the ADMIN role. */
@RestController
@RequestMapping("/api/admin/jobs")
public class AdminJobController {

    private final JobService jobService;
    private final CurrentUserService currentUserService;

    public AdminJobController(
            JobService jobService,
            CurrentUserService currentUserService) {
        this.jobService = jobService;
        this.currentUserService = currentUserService;
    }

    /** Optional filter ?approvalStatus=PENDING|APPROVED|REJECTED. */
    @GetMapping
    public ResponseEntity<List<JobDTO>> getJobs(
            @RequestParam(required = false) String approvalStatus) {
        return ResponseEntity.ok(jobService.getAllJobsForAdmin(approvalStatus));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<JobDTO>> getPendingJobs() {
        return ResponseEntity.ok(jobService.getAllJobsForAdmin("PENDING"));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<JobDTO> approve(@PathVariable Integer id) {
        return ResponseEntity.ok(jobService.approveJob(currentUserService.getUser(), id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<JobDTO> reject(
            @PathVariable Integer id,
            @RequestBody(required = false) RejectRequest request) {
        return ResponseEntity.ok(jobService.rejectJob(
                currentUserService.getUser(), id,
                request == null ? null : request.reason()));
    }
}
