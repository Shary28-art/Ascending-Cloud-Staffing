package com.ascendingcloud.staffing.dto;

import java.time.LocalDateTime;

/** Resume metadata only. The file itself lives in object storage. */
public record ResumeDTO(
        Integer id,
        Integer candidateId,
        String originalFileName,
        String contentType,
        Long fileSize,
        LocalDateTime uploadedAt,
        String downloadUrl
) {
}
