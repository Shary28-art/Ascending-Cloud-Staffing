package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.Job;
import com.ascendingcloud.staffing.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<Job> createJob(
            @RequestBody JobRequest request) {

        return ResponseEntity.ok(
                jobService.createJob(
                        request.title(),
                        request.companyId(),
                        request.location(),
                        request.salary(),
                        request.cloudPlatformId(),
                        request.employmentTypeId(),
                        request.experienceLevelId(),
                        request.workplaceTypeId(),
                        request.description()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(
                jobService.getAllJobs()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                jobService.getJob(id)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location) {

        return ResponseEntity.ok(
                jobService.searchJobs(title, location)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(
            @PathVariable Integer id,
            @RequestBody JobRequest request) {

        return ResponseEntity.ok(
                jobService.updateJob(
                        id,
                        request.title(),
                        request.companyId(),
                        request.location(),
                        request.salary(),
                        request.cloudPlatformId(),
                        request.employmentTypeId(),
                        request.experienceLevelId(),
                        request.workplaceTypeId(),
                        request.description()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Integer id) {

        jobService.deleteJob(id);

        return ResponseEntity.noContent().build();
    }

    public record JobRequest(
            String title,
            Integer companyId,
            String location,
            String salary,
            Integer cloudPlatformId,
            Integer employmentTypeId,
            Integer experienceLevelId,
            Integer workplaceTypeId,
            String description
    ) {
    }
}