package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.ExperienceLevel;
import com.ascendingcloud.staffing.repository.ExperienceLevelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceLevelService {

    private final ExperienceLevelRepository experienceLevelRepository;

    public ExperienceLevelService(
            ExperienceLevelRepository experienceLevelRepository) {
        this.experienceLevelRepository = experienceLevelRepository;
    }

    public List<ExperienceLevel> getAllExperienceLevels() {
        return experienceLevelRepository.findAll();
    }

    public ExperienceLevel getExperienceLevelById(Integer id) {
        return experienceLevelRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Experience level not found"));
    }

    public ExperienceLevel createExperienceLevel(
            ExperienceLevel experienceLevel) {
        return experienceLevelRepository.save(experienceLevel);
    }

    public ExperienceLevel updateExperienceLevel(
            Integer id,
            ExperienceLevel experienceLevel) {

        ExperienceLevel existingExperienceLevel =
                getExperienceLevelById(id);

        existingExperienceLevel.setName(experienceLevel.getName());

        return experienceLevelRepository.save(existingExperienceLevel);
    }

    public void deleteExperienceLevel(Integer id) {
        if (!experienceLevelRepository.existsById(id)) {
            throw new RuntimeException("Experience level not found");
        }

        experienceLevelRepository.deleteById(id);
    }
}