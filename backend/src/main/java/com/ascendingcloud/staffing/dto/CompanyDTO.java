package com.ascendingcloud.staffing.dto;

/** Request body for create/update (id and logoUrl are ignored) and read model. */
public record CompanyDTO(
        Integer id,
        String name,
        String description,
        String website,
        String industry,
        String companySize,
        String location,
        String logoUrl
) {
}
