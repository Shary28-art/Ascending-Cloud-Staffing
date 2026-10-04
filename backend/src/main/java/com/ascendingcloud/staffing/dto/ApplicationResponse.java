package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public class ApplicationResponse {

    private Integer id;
    private Integer jobId;
    private String jobTitle;
    private Integer candidateId;
    private String candidateName;
    private String status;
    private LocalDateTime appliedAt;

    public ApplicationResponse() {
    }

    public ApplicationResponse(
            Integer id,
            Integer jobId,
            String jobTitle,
            Integer candidateId,
            String candidateName,
            String status,
            LocalDateTime appliedAt) {

        this.id = id;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.candidateId = candidateId;
        this.candidateName = candidateName;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
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