package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.JobCategory;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.JobCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobCategoryService {

    private final JobCategoryRepository repository;

    public JobCategoryService(JobCategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<JobCategory> getAllJobCategories() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public JobCategory getJobCategoryById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job category not found with id: " + id));
    }

    @Transactional
    public JobCategory createJobCategory(JobCategory entity) {
        String name = cleanName(entity.getName());

        JobCategory created = new JobCategory();
        created.setName(name);
        created.setDescription(entity.getDescription());

        return repository.save(created);
    }

    @Transactional
    public JobCategory updateJobCategory(Integer id, JobCategory entity) {
        JobCategory existing = getJobCategoryById(id);

        existing.setName(cleanName(entity.getName()));
        existing.setDescription(entity.getDescription());

        return repository.save(existing);
    }

    @Transactional
    public void deleteJobCategory(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Job category not found with id: " + id);
        }

        repository.deleteById(id);
    }

    private String cleanName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Job category name is required");
        }

        return name.trim();
    }
}
