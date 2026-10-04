package com.ascendingcloud.staffing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "candidate_skills")
@IdClass(CandidateSkillId.class)
public class CandidateSkill {

    @Id
    @Column(name = "candidate_id")
    private Integer candidateId;

    @Id
    @Column(name = "skill_id")
    private Integer skillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "candidate_id",
            insertable = false,
            updatable = false
    )
    private CandidateProfile candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "skill_id",
            insertable = false,
            updatable = false
    )
    private Skill skill;

    public CandidateSkill() {
    }

    public CandidateSkill(
            Integer candidateId,
            Integer skillId,
            CandidateProfile candidate,
            Skill skill
    ) {
        this.candidateId = candidateId;
        this.skillId = skillId;
        this.candidate = candidate;
        this.skill = skill;
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

    public CandidateProfile getCandidate() {
        return candidate;
    }

    public void setCandidate(CandidateProfile candidate) {
        this.candidate = candidate;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}