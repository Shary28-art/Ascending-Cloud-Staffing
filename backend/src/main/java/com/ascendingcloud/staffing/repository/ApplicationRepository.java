package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    boolean existsByJobIdAndCandidateId(Integer jobId, Integer candidateId);

    List<Application> findByCandidateIdOrderByAppliedAtDesc(Integer candidateId);

    List<Application> findByJobIdOrderByAppliedAtDesc(Integer jobId);
}