package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Integer> {

    List<SavedJob> findByUserIdOrderBySavedAtDesc(Integer userId);

    Optional<SavedJob> findByUserIdAndJobId(Integer userId, Integer jobId);

    boolean existsByUserIdAndJobId(Integer userId, Integer jobId);
}
