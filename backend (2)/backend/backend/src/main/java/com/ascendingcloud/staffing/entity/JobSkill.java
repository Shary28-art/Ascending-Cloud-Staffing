package com.ascendingcloud.staffing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_skills")
@IdClass(JobSkillId.class)
public class JobSkill {

    @Id
    @Column(name = "job_id")
    private Integer jobId;

    @Id
    @Column(name = "skill_id")
    private Integer skillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "job_id",
            insertable = false,
            updatable = false
    )
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "skill_id",
            insertable = false,
            updatable = false
    )
    private Skill skill;

    public JobSkill() {
    }

    public JobSkill(
            Integer jobId,
            Integer skillId,
            Job job,
            Skill skill) {

        this.jobId = jobId;
        this.skillId = skillId;
        this.job = job;
        this.skill = skill;
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

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}