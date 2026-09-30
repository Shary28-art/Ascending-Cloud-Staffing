package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.CloudPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CloudPlatformRepository
        extends JpaRepository<CloudPlatform, Integer> {
}