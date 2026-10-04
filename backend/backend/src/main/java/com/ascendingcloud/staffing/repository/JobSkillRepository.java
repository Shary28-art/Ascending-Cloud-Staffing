package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.JobSkill;
import com.ascendingcloud.staffing.entity.JobSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSkillRepository
        extends JpaRepository<JobSkill, JobSkillId> {

    List<JobSkill> findByJobId(Integer jobId);

    boolean existsByJobIdAndSkillId(
            Integer jobId,
            Integer skillId
    );

    void deleteByJobIdAndSkillId(
            Integer jobId,
            Integer skillId
    );
}