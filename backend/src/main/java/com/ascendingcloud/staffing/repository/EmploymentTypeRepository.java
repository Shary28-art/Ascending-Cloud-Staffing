package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.EmploymentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploymentTypeRepository
        extends JpaRepository<EmploymentType, Integer> {
}