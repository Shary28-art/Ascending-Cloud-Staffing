package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public class ApplicationResponse {

    private Integer id;
    private Integer jobId;
    private Integer candidateId;
    private String status;
    private LocalDateTime appliedAt;

    public ApplicationResponse() {
    }

    public ApplicationResponse(
            Integer id,
            Integer jobId,
            Integer candidateId,
            String status,
            LocalDateTime appliedAt) {
        this.id = id;
        this.jobId = jobId;
        this.candidateId = candidateId;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }
}