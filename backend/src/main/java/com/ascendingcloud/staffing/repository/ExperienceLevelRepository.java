package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.ExperienceLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceLevelRepository
        extends JpaRepository<ExperienceLevel, Integer> {
}