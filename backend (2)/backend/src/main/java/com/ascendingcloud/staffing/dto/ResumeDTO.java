package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

public record ResumeDTO(
        Integer id,
        Integer candidateId,
        String fileName,
        String filePath,
        LocalDateTime uploadedAt
) {}