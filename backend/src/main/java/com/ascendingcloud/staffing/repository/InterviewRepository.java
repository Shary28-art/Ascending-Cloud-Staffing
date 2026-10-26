package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Integer> {

    List<Interview> findAllByOrderByScheduledAtAsc();

    List<Interview> findByApplicationCandidateUserIdOrderByScheduledAtAsc(Integer userId);

    List<Interview> findByApplicationJobCompanyIdOrderByScheduledAtAsc(Integer companyId);
}
