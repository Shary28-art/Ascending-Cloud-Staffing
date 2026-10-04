package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.EmployerProfileDTO;
import com.ascendingcloud.staffing.entity.Company;
import com.ascendingcloud.staffing.entity.EmployerProfile;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.CompanyRepository;
import com.ascendingcloud.staffing.repository.EmployerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployerProfileService {

    private final EmployerProfileRepository employerProfileRepository;
    private final CompanyRepository companyRepository;

    public EmployerProfileService(
            EmployerProfileRepository employerProfileRepository,
            CompanyRepository companyRepository) {
        this.employerProfileRepository = employerProfileRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional
    public EmployerProfile createProfile(User user, EmployerProfileDTO dto) {

        if (employerProfileRepository.existsByUserId(user.getId())) {
            throw new BadRequestException(
                    "Employer profile already exists. Use PUT /employer-profiles/me to update it");
        }

        EmployerProfile profile = new EmployerProfile();
        profile.setUser(user);

        apply(profile, dto, true);

        return employerProfileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public EmployerProfile getProfile(User user) {
        return employerProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employer profile not found"));
    }

    @Transactional
    public EmployerProfile updateProfile(User user, EmployerProfileDTO dto) {
        EmployerProfile profile = getProfile(user);

        apply(profile, dto, false);

        return employerProfileRepository.save(profile);
    }

    /** Only non-null values are applied, so partial updates are safe. */
    private void apply(EmployerProfile p, EmployerProfileDTO dto, boolean creating) {

        if (dto == null) {
            if (creating) {
                return;
            }
            throw new BadRequestException("Request body is required");
        }

        if (dto.companyId() != null) {
            Company company = companyRepository.findById(dto.companyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

            // An employer may only attach themselves to a company they do not
            // already belong to when creating the profile for the first time.
            if (!creating
                    && p.getCompany() != null
                    && !p.getCompany().getId().equals(company.getId())) {
                throw new BadRequestException(
                        "Your profile is already linked to a different company");
            }

            p.setCompany(company);
        }

        if (dto.phone() != null) p.setPhone(dto.phone());
        if (dto.designation() != null) p.setDesignation(dto.designation());
        if (dto.primaryContactName() != null) p.setPrimaryContactName(dto.primaryContactName());
        if (dto.contactEmail() != null) p.setContactEmail(dto.contactEmail());
        if (dto.hiringLocations() != null) p.setHiringLocations(dto.hiringLocations());
        if (dto.employeeTypesNeeded() != null) p.setEmployeeTypesNeeded(dto.employeeTypesNeeded());
        if (dto.preferredWorkArrangements() != null) p.setPreferredWorkArrangements(dto.preferredWorkArrangements());
        if (dto.primaryRoles() != null) p.setPrimaryRoles(dto.primaryRoles());
        if (dto.expectedHires() != null) p.setExpectedHires(dto.expectedHires());
        if (dto.hiringTimeline() != null) p.setHiringTimeline(dto.hiringTimeline());
        if (dto.billingAddress() != null) p.setBillingAddress(dto.billingAddress());
        if (dto.selectedPlan() != null) p.setSelectedPlan(dto.selectedPlan());
        if (dto.preferredInterviewMethod() != null) p.setPreferredInterviewMethod(dto.preferredInterviewMethod());
        if (dto.placementTermsAccepted() != null) p.setPlacementTermsAccepted(dto.placementTermsAccepted());
        if (dto.notificationPreferences() != null) p.setNotificationPreferences(dto.notificationPreferences());
    }
}
