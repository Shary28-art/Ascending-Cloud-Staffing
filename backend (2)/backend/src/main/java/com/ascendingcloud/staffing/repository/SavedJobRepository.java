package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Integer> {

    List<SavedJob> findByCandidateId(Integer candidateId);

    boolean existsByCandidateIdAndJobId(Integer candidateId, Integer jobId);
}