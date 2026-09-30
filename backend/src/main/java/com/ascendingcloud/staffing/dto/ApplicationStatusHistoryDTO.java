package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public class ApplicationStatusHistoryDTO {

    private Integer id;
    private Integer applicationId;
    private String status;
    private LocalDateTime changedAt;

    public ApplicationStatusHistoryDTO() {
    }

    public ApplicationStatusHistoryDTO(
            Integer id,
            Integer applicationId,
            String status,
            LocalDateTime changedAt) {
        this.id = id;
        this.applicationId = applicationId;
        this.status = status;
        this.changedAt = changedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}