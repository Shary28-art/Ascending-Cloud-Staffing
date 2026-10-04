package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public record SavedJobDTO(
        Integer id,
        Integer candidateId,
        Integer jobId,
        LocalDateTime savedAt
) {}