package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Company;
import com.ascendingcloud.staffing.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(
            String name,
            String description,
            String website,
            String location) {

        Company company = new Company();

        company.setName(name);
        company.setDescription(description);
        company.setWebsite(website);
        company.setLocation(location);

        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompany(Integer id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    public Company updateCompany(
            Integer id,
            String name,
            String description,
            String website,
            String location) {

        Company company = getCompany(id);

        company.setName(name);
        company.setDescription(description);
        company.setWebsite(website);
        company.setLocation(location);

        return companyRepository.save(company);
    }

    public void deleteCompany(Integer id) {
        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("Company not found");
        }

        companyRepository.deleteById(id);
    }
}