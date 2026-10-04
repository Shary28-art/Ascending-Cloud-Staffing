package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.ExperienceLevel;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.ExperienceLevelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExperienceLevelService {

    private final ExperienceLevelRepository repository;

    public ExperienceLevelService(ExperienceLevelRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ExperienceLevel> getAllExperienceLevels() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ExperienceLevel getExperienceLevelById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Experience level not found with id: " + id));
    }

    @Transactional
    public ExperienceLevel createExperienceLevel(ExperienceLevel entity) {
        String name = cleanName(entity.getName());

        ExperienceLevel created = new ExperienceLevel();
        created.setName(name);

        return repository.save(created);
    }

    @Transactional
    public ExperienceLevel updateExperienceLevel(Integer id, ExperienceLevel entity) {
        ExperienceLevel existing = getExperienceLevelById(id);

        existing.setName(cleanName(entity.getName()));

        return repository.save(existing);
    }

    @Transactional
    public void deleteExperienceLevel(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Experience level not found with id: " + id);
        }

        repository.deleteById(id);
    }

    private String cleanName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Experience level name is required");
        }

        return name.trim();
    }
}
