package com.ascendingcloud.staffing.dto;

public record CompanyDTO(
        Integer id,
        String name,
        String description,
        String website,
        String industry,
        String companySize,
        String location
) {
}