package com.ascendingcloud.staffing.mapper;

import com.ascendingcloud.staffing.dto.ApplicationDTO;
import com.ascendingcloud.staffing.entity.Application;

public class ApplicationMapper {

    private ApplicationMapper() {
    }

    public static ApplicationDTO toDTO(Application application) {
        if (application == null) {
            return null;
        }

        return new ApplicationDTO(
                application.getId(),
                application.getJob() != null
                        ? application.getJob().getId()
                        : null,
                application.getCandidate() != null
                        ? application.getCandidate().getId()
                        : null,
                application.getStatus(),
                application.getAppliedAt()
        );
    }
}