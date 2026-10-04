package com.ascendingcloud.staffing.entity;

import java.io.Serializable;
import java.util.Objects;

public class CandidateSkillId implements Serializable {

    private Integer candidateId;
    private Integer skillId;

    public CandidateSkillId() {
    }

    public CandidateSkillId(Integer candidateId, Integer skillId) {
        this.candidateId = candidateId;
        this.skillId = skillId;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CandidateSkillId that)) {
            return false;
        }

        return Objects.equals(candidateId, that.candidateId)
                && Objects.equals(skillId, that.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidateId, skillId);
    }
}