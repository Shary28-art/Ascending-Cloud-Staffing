package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.WorkplaceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkplaceTypeRepository
        extends JpaRepository<WorkplaceType, Integer> {
}