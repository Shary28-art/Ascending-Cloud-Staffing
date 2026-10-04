package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    boolean existsByJobIdAndCandidateId(Integer jobId, Integer candidateId);

    List<Application> findAllByOrderByAppliedAtDesc();

    List<Application> findByCandidateIdOrderByAppliedAtDesc(Integer candidateId);

    List<Application> findByJobIdOrderByAppliedAtDesc(Integer jobId);

    /** Used to decide whether an employer may see a candidate's resume. */
    boolean existsByCandidateIdAndJobCompanyId(
            Integer candidateId,
            Integer companyId
    );

    List<Application> findByJobCompanyIdOrderByAppliedAtDesc(Integer companyId);
}
