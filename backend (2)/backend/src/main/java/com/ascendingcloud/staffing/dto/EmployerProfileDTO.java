package com.ascendingcloud.staffing.dto;

public record EmployerProfileDTO(
        Integer id,
        Integer userId,
        String phone,
        String designation,
        Integer companyId
) {}