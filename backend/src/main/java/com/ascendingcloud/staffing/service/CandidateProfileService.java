package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.CandidateProfileRequest;
import com.ascendingcloud.staffing.dto.CandidateProfileResponse;
import com.ascendingcloud.staffing.entity.CandidateProfile;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.mapper.CandidateProfileMapper;
import com.ascendingcloud.staffing.repository.ApplicationRepository;
import com.ascendingcloud.staffing.repository.CandidateProfileRepository;
import com.ascendingcloud.staffing.security.CurrentUserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final ApplicationRepository applicationRepository;
    private final CurrentUserService currentUserService;

    public CandidateProfileService(
            CandidateProfileRepository candidateProfileRepository,
            ApplicationRepository applicationRepository,
            CurrentUserService currentUserService) {
        this.candidateProfileRepository = candidateProfileRepository;
        this.applicationRepository = applicationRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public CandidateProfileResponse createProfile(User user, CandidateProfileRequest req) {

        if (!currentUserService.isCandidate(user)) {
            throw new AccessDeniedException("Only candidates can create a candidate profile");
        }

        if (candidateProfileRepository.existsByUserId(user.getId())) {
            throw new BadRequestException(
                    "Candidate profile already exists. Use PUT /candidate-profiles/me to update it");
        }

        CandidateProfile profile = new CandidateProfile();
        profile.setUser(user);

        apply(profile, req);

        return CandidateProfileMapper.toResponse(candidateProfileRepository.save(profile));
    }

    @Transactional(readOnly = true)
    public CandidateProfileResponse getMyProfile(User user) {
        return CandidateProfileMapper.toResponse(findByUser(user));
    }

    @Transactional
    public CandidateProfileResponse updateMyProfile(User user, CandidateProfileRequest req) {
        CandidateProfile profile = findByUser(user);

        apply(profile, req);

        return CandidateProfileMapper.toResponse(candidateProfileRepository.save(profile));
    }

    /**
     * Employer/admin view of a candidate. Employers only see candidates who
     * agreed to share their profile, or who applied to one of their jobs.
     */
    @Transactional(readOnly = true)
    public CandidateProfileResponse getProfileForViewer(User viewer, Integer candidateId) {
        CandidateProfile profile = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));

        if (currentUserService.isAdmin(viewer)) {
            return CandidateProfileMapper.toResponse(profile);
        }

        if (currentUserService.isCandidate(viewer)
                && profile.getUser().getId().equals(viewer.getId())) {
            return CandidateProfileMapper.toResponse(profile);
        }

        if (currentUserService.isEmployer(viewer)) {
            Integer companyId = currentUserService.getEmployerCompanyId(viewer);

            boolean applied = companyId != null
                    && applicationRepository.existsByCandidateIdAndJobCompanyId(
                            candidateId, companyId);

            boolean shared = Boolean.TRUE.equals(profile.getProfileVisibility())
                    && Boolean.TRUE.equals(profile.getShareWithEmployers());

            if (applied || shared) {
                return CandidateProfileMapper.toResponse(profile);
            }
        }

        throw new AccessDeniedException("You are not allowed to view this profile");
    }

    private CandidateProfile findByUser(User user) {
        return candidateProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));
    }

    /** Only non-null request values are applied, so partial updates are safe. */
    private void apply(CandidateProfile p, CandidateProfileRequest r) {

        if (r == null) {
            throw new BadRequestException("Request body is required");
        }

        if (r.phone() != null) p.setPhone(r.phone());
        if (r.location() != null) p.setLocation(r.location());
        if (r.city() != null) p.setCity(r.city());
        if (r.state() != null) p.setState(r.state());
        if (r.country() != null) p.setCountry(r.country());
        if (r.bio() != null) p.setBio(r.bio());
        if (r.professionalHeadline() != null) p.setProfessionalHeadline(r.professionalHeadline());
        if (r.professionalSummary() != null) p.setProfessionalSummary(r.professionalSummary());
        if (r.currentJobTitle() != null) p.setCurrentJobTitle(r.currentJobTitle());
        if (r.totalYearsExperience() != null) p.setTotalYearsExperience(r.totalYearsExperience());
        if (r.desiredJobTitle() != null) p.setDesiredJobTitle(r.desiredJobTitle());
        if (r.preferredEmploymentType() != null) p.setPreferredEmploymentType(r.preferredEmploymentType());
        if (r.preferredWorkArrangement() != null) p.setPreferredWorkArrangement(r.preferredWorkArrangement());
        if (r.preferredJobLocation() != null) p.setPreferredJobLocation(r.preferredJobLocation());
        if (r.salaryExpectation() != null) p.setSalaryExpectation(r.salaryExpectation());
        if (r.availabilityDate() != null) p.setAvailabilityDate(r.availabilityDate());
        if (r.workAuthorizationStatus() != null) p.setWorkAuthorizationStatus(r.workAuthorizationStatus());
        if (r.sponsorshipRequired() != null) p.setSponsorshipRequired(r.sponsorshipRequired());
        if (r.securityClearanceStatus() != null) p.setSecurityClearanceStatus(r.securityClearanceStatus());
        if (r.linkedinPortfolioUrl() != null) p.setLinkedinPortfolioUrl(r.linkedinPortfolioUrl());
        if (r.profileVisibility() != null) p.setProfileVisibility(r.profileVisibility());
        if (r.shareWithEmployers() != null) p.setShareWithEmployers(r.shareWithEmployers());
    }
}
