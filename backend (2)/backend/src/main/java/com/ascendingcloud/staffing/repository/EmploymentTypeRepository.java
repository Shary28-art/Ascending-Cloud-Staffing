package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.EmploymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmploymentTypeRepository
        extends JpaRepository<EmploymentType, Integer> {

    Optional<EmploymentType> findByName(String name);

    boolean existsByName(String name);
}