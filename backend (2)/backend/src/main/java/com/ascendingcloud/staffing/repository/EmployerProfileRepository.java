package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.EmployerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Integer> {

    Optional<EmployerProfile> findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);
}