package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.JobCategory;
import com.ascendingcloud.staffing.repository.JobCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobCategoryService {

    private final JobCategoryRepository jobCategoryRepository;

    public JobCategoryService(JobCategoryRepository jobCategoryRepository) {
        this.jobCategoryRepository = jobCategoryRepository;
    }

    public List<JobCategory> getAllCategories() {
        return jobCategoryRepository.findAll();
    }

    public Optional<JobCategory> getCategoryById(Integer id) {
        return jobCategoryRepository.findById(id);
    }

    public JobCategory createCategory(JobCategory category) {
        return jobCategoryRepository.save(category);
    }

    public JobCategory updateCategory(
            Integer id,
            JobCategory details) {

        Optional<JobCategory> optional =
                jobCategoryRepository.findById(id);

        if (optional.isEmpty()) {
            return null;
        }

        JobCategory category = optional.get();

        category.setName(details.getName());
        category.setDescription(details.getDescription());

        return jobCategoryRepository.save(category);
    }

    public boolean deleteCategory(Integer id) {
        if (!jobCategoryRepository.existsById(id)) {
            return false;
        }

        jobCategoryRepository.deleteById(id);
        return true;
    }
}