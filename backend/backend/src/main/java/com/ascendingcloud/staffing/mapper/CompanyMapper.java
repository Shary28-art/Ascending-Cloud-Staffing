package com.ascendingcloud.staffing.mapper;

import com.ascendingcloud.staffing.dto.CompanyDTO;
import com.ascendingcloud.staffing.entity.Company;

public class CompanyMapper {

    private CompanyMapper() {
    }

    public static CompanyDTO toDTO(Company company) {
        if (company == null) {
            return null;
        }

        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getWebsite(),
                company.getIndustry(),
                company.getCompanySize(),
                company.getLocation()
        );
    }
}