package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.JobCategory;
import com.ascendingcloud.staffing.service.JobCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/job-categories")
@CrossOrigin(origins = "*")
public class JobCategoryController {

    private final JobCategoryService jobCategoryService;

    public JobCategoryController(
            JobCategoryService jobCategoryService) {
        this.jobCategoryService = jobCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<JobCategory>>
    getAllCategories() {

        return ResponseEntity.ok(
                jobCategoryService.getAllCategories()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobCategory>
    getCategoryById(@PathVariable Integer id) {

        Optional<JobCategory> result =
                jobCategoryService.getCategoryById(id);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result.get());
    }

    @PostMapping
    public ResponseEntity<JobCategory>
    createCategory(@RequestBody JobCategory category) {

        return ResponseEntity.ok(
                jobCategoryService.createCategory(category)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobCategory>
    updateCategory(
            @PathVariable Integer id,
            @RequestBody JobCategory category) {

        JobCategory updated =
                jobCategoryService.updateCategory(
                        id,
                        category
                );

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteCategory(@PathVariable Integer id) {

        if (!jobCategoryService.deleteCategory(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}