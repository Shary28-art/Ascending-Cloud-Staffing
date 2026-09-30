package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
}