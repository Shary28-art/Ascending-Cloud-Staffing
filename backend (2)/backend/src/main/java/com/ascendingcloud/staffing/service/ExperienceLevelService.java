package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.ExperienceLevel;
import com.ascendingcloud.staffing.repository.ExperienceLevelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<ExperienceLevel> getExperienceLevelById(Integer id) {
        return experienceLevelRepository.findById(id);
    }

    public ExperienceLevel createExperienceLevel(
            ExperienceLevel experienceLevel) {
        return experienceLevelRepository.save(experienceLevel);
    }

    public ExperienceLevel updateExperienceLevel(
            Integer id,
            ExperienceLevel details) {

        Optional<ExperienceLevel> optional =
                experienceLevelRepository.findById(id);

        if (optional.isEmpty()) {
            return null;
        }

        ExperienceLevel experienceLevel = optional.get();
        experienceLevel.setName(details.getName());

        return experienceLevelRepository.save(experienceLevel);
    }

    public boolean deleteExperienceLevel(Integer id) {
        if (!experienceLevelRepository.existsById(id)) {
            return false;
        }

        experienceLevelRepository.deleteById(id);
        return true;
    }
}