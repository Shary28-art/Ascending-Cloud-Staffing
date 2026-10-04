package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Company;
import com.ascendingcloud.staffing.entity.EmployerProfile;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.repository.CompanyRepository;
import com.ascendingcloud.staffing.repository.EmployerProfileRepository;
import com.ascendingcloud.staffing.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployerProfileService {

    private final EmployerProfileRepository employerProfileRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public EmployerProfileService(
            EmployerProfileRepository employerProfileRepository,
            UserRepository userRepository,
            CompanyRepository companyRepository) {

        this.employerProfileRepository = employerProfileRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public EmployerProfile createProfile(
            String email,
            String phone,
            String designation,
            Integer companyId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"EMPLOYER".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException(
                    "Only employers can create employer profiles");
        }

        if (employerProfileRepository.existsByUserId(user.getId())) {
            throw new RuntimeException(
                    "Employer profile already exists");
        }

        Company company = null;

        if (companyId != null) {
            company = companyRepository.findById(companyId)
                    .orElseThrow(() ->
                            new RuntimeException("Company not found"));
        }

        EmployerProfile profile = new EmployerProfile();

        profile.setUser(user);
        profile.setPhone(phone);
        profile.setDesignation(designation);
        profile.setCompany(company);

        return employerProfileRepository.save(profile);
    }

    public EmployerProfile getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return employerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Employer profile not found"));
    }

    public EmployerProfile updateProfile(
            String email,
            String phone,
            String designation,
            Integer companyId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EmployerProfile profile =
                employerProfileRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employer profile not found"));

        Company company = null;

        if (companyId != null) {
            company = companyRepository.findById(companyId)
                    .orElseThrow(() ->
                            new RuntimeException("Company not found"));
        }

        profile.setPhone(phone);
        profile.setDesignation(designation);
        profile.setCompany(company);

        return employerProfileRepository.save(profile);
    }
}