package com.ascendingcloud.staffing.entity;

import java.io.Serializable;
import java.util.Objects;

public class JobSkillId implements Serializable {

    private Integer jobId;
    private Integer skillId;

    public JobSkillId() {
    }

    public JobSkillId(Integer jobId, Integer skillId) {
        this.jobId = jobId;
        this.skillId = skillId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
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

        if (!(o instanceof JobSkillId that)) return false;

        return Objects.equals(jobId, that.jobId)
                && Objects.equals(skillId, that.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, skillId);
    }
}