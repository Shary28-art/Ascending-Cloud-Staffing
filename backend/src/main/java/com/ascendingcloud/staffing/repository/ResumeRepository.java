package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {

    Optional<Resume> findByCandidateId(Integer candidateId);
}
