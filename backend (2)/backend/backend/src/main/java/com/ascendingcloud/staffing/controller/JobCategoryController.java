package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.JobCategory;
import com.ascendingcloud.staffing.service.JobCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-categories")
@CrossOrigin(origins = "*")
public class JobCategoryController {

    private final JobCategoryService jobCategoryService;

    public JobCategoryController(JobCategoryService jobCategoryService) {
        this.jobCategoryService = jobCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<JobCategory>> getAllCategories() {
        return ResponseEntity.ok(jobCategoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobCategory> getCategoryById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                jobCategoryService.getCategoryById(id)
        );
    }

    @PostMapping
    public ResponseEntity<JobCategory> createCategory(
            @RequestBody JobCategory category) {
        return ResponseEntity.ok(
                jobCategoryService.createCategory(category)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobCategory> updateCategory(
            @PathVariable Integer id,
            @RequestBody JobCategory category) {
        return ResponseEntity.ok(
                jobCategoryService.updateCategory(id, category)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Integer id) {
        jobCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}