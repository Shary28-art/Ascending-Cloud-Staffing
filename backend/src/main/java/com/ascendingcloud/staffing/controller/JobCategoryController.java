package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.JobCategory;
import com.ascendingcloud.staffing.service.JobCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-categories")
public class JobCategoryController {

    private final JobCategoryService service;

    public JobCategoryController(JobCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<JobCategory>> getAllJobCategories() {
        return ResponseEntity.ok(service.getAllJobCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobCategory> getJobCategoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getJobCategoryById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobCategory> createJobCategory(@RequestBody JobCategory body) {
        return ResponseEntity.status(201).body(service.createJobCategory(body));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobCategory> updateJobCategory(
            @PathVariable Integer id,
            @RequestBody JobCategory body) {
        return ResponseEntity.ok(service.updateJobCategory(id, body));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteJobCategory(@PathVariable Integer id) {
        service.deleteJobCategory(id);
        return ResponseEntity.noContent().build();
    }
}
