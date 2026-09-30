package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.CandidateProfileResponse;
import com.ascendingcloud.staffing.entity.CandidateProfile;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.repository.CandidateProfileRepository;
import com.ascendingcloud.staffing.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;

    public CandidateProfileService(
            CandidateProfileRepository candidateProfileRepository,
            UserRepository userRepository) {

        this.candidateProfileRepository = candidateProfileRepository;
        this.userRepository = userRepository;
    }

    public CandidateProfileResponse createProfile(
            String email,
            String phone,
            String location,
            String bio,
            String professionalHeadline,
            String professionalSummary,
            String currentJobTitle,
            BigDecimal totalYearsExperience,
            String desiredJobTitle,
            String preferredEmploymentType,
            String preferredWorkArrangement,
            String preferredJobLocation,
            String salaryExpectation,
            LocalDate availabilityDate,
            String workAuthorizationStatus,
            Boolean sponsorshipRequired,
            String securityClearanceStatus,
            Boolean profileVisibility) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!"CANDIDATE".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException(
                    "Only candidates can create a candidate profile");
        }

        if (candidateProfileRepository.existsByUserId(user.getId())) {
            throw new RuntimeException(
                    "Candidate profile already exists");
        }

        CandidateProfile profile = new CandidateProfile();

        profile.setUser(user);
        profile.setPhone(phone);
        profile.setLocation(location);
        profile.setBio(bio);
        profile.setProfessionalHeadline(professionalHeadline);
        profile.setProfessionalSummary(professionalSummary);
        profile.setCurrentJobTitle(currentJobTitle);
        profile.setTotalYearsExperience(totalYearsExperience);
        profile.setDesiredJobTitle(desiredJobTitle);
        profile.setPreferredEmploymentType(preferredEmploymentType);
        profile.setPreferredWorkArrangement(preferredWorkArrangement);
        profile.setPreferredJobLocation(preferredJobLocation);
        profile.setSalaryExpectation(salaryExpectation);
        profile.setAvailabilityDate(availabilityDate);
        profile.setWorkAuthorizationStatus(workAuthorizationStatus);
        profile.setSponsorshipRequired(sponsorshipRequired);
        profile.setSecurityClearanceStatus(securityClearanceStatus);

        if (profileVisibility != null) {
            profile.setProfileVisibility(profileVisibility);
        }

        CandidateProfile savedProfile =
                candidateProfileRepository.save(profile);

        return toResponse(savedProfile);
    }

    public CandidateProfileResponse getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        CandidateProfile profile =
                candidateProfileRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Candidate profile not found"));

        return toResponse(profile);
    }

    public CandidateProfileResponse updateProfile(
            String email,
            String phone,
            String location,
            String bio,
            String professionalHeadline,
            String professionalSummary,
            String currentJobTitle,
            BigDecimal totalYearsExperience,
            String desiredJobTitle,
            String preferredEmploymentType,
            String preferredWorkArrangement,
            String preferredJobLocation,
            String salaryExpectation,
            LocalDate availabilityDate,
            String workAuthorizationStatus,
            Boolean sponsorshipRequired,
            String securityClearanceStatus,
            Boolean profileVisibility) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        CandidateProfile profile =
                candidateProfileRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Candidate profile not found"));

        profile.setPhone(phone);
        profile.setLocation(location);
        profile.setBio(bio);
        profile.setProfessionalHeadline(professionalHeadline);
        profile.setProfessionalSummary(professionalSummary);
        profile.setCurrentJobTitle(currentJobTitle);
        profile.setTotalYearsExperience(totalYearsExperience);
        profile.setDesiredJobTitle(desiredJobTitle);
        profile.setPreferredEmploymentType(preferredEmploymentType);
        profile.setPreferredWorkArrangement(preferredWorkArrangement);
        profile.setPreferredJobLocation(preferredJobLocation);
        profile.setSalaryExpectation(salaryExpectation);
        profile.setAvailabilityDate(availabilityDate);
        profile.setWorkAuthorizationStatus(workAuthorizationStatus);
        profile.setSponsorshipRequired(sponsorshipRequired);
        profile.setSecurityClearanceStatus(securityClearanceStatus);

        if (profileVisibility != null) {
            profile.setProfileVisibility(profileVisibility);
        }

        CandidateProfile updatedProfile =
                candidateProfileRepository.save(profile);

        return toResponse(updatedProfile);
    }

    private CandidateProfileResponse toResponse(
            CandidateProfile profile) {

        return new CandidateProfileResponse(
                profile.getId(),
                profile.getPhone(),
                profile.getLocation(),
                profile.getBio(),
                profile.getProfessionalHeadline(),
                profile.getProfessionalSummary(),
                profile.getCurrentJobTitle(),
                profile.getTotalYearsExperience(),
                profile.getDesiredJobTitle(),
                profile.getPreferredEmploymentType(),
                profile.getPreferredWorkArrangement(),
                profile.getPreferredJobLocation(),
                profile.getSalaryExpectation(),
                profile.getAvailabilityDate(),
                profile.getWorkAuthorizationStatus(),
                profile.getSponsorshipRequired(),
                profile.getSecurityClearanceStatus(),
                profile.getProfileVisibility()
        );
    }
}