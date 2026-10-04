package com.ascendingcloud.staffing.mapper;

import com.ascendingcloud.staffing.dto.ResumeDTO;
import com.ascendingcloud.staffing.entity.Resume;

public class ResumeMapper {

    private ResumeMapper() {
    }

    public static ResumeDTO toDTO(Resume resume) {
        if (resume == null) {
            return null;
        }

        return new ResumeDTO(
                resume.getId(),
                resume.getCandidate() != null ? resume.getCandidate().getId() : null,
                resume.getOriginalFileName(),
                resume.getContentType(),
                resume.getFileSize(),
                resume.getUploadedAt(),
                "/api/resumes/" + resume.getId() + "/download"
        );
    }
}
