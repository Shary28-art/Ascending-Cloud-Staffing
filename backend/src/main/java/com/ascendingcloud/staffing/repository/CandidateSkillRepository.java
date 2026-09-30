package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.CandidateSkill;
import com.ascendingcloud.staffing.entity.CandidateSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateSkillRepository
        extends JpaRepository<CandidateSkill, CandidateSkillId> {

    List<CandidateSkill> findByCandidateId(Integer candidateId);

    boolean existsByCandidateIdAndSkillId(
            Integer candidateId,
            Integer skillId
    );

    void deleteByCandidateIdAndSkillId(
            Integer candidateId,
            Integer skillId
    );
}