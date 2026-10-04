package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.JobCategory;
import com.ascendingcloud.staffing.repository.JobCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobCategoryService {

    private final JobCategoryRepository jobCategoryRepository;

    public JobCategoryService(JobCategoryRepository jobCategoryRepository) {
        this.jobCategoryRepository = jobCategoryRepository;
    }

    public List<JobCategory> getAllCategories() {
        return jobCategoryRepository.findAll();
    }

    public JobCategory getCategoryById(Integer id) {
        return jobCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job category not found"));
    }

    public JobCategory createCategory(JobCategory category) {
        return jobCategoryRepository.save(category);
    }

    public JobCategory updateCategory(
            Integer id,
            JobCategory category) {

        JobCategory existingCategory = getCategoryById(id);

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        return jobCategoryRepository.save(existingCategory);
    }

    public void deleteCategory(Integer id) {
        if (!jobCategoryRepository.existsById(id)) {
            throw new RuntimeException("Job category not found");
        }

        jobCategoryRepository.deleteById(id);
    }
}