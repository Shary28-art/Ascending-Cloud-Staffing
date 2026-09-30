package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateProfileRepository
        extends JpaRepository<CandidateProfile, Integer> {

    Optional<CandidateProfile> findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);
}