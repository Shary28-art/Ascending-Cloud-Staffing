package com.ascendingcloud.staffing.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "candidate_profiles")
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 20)
    private String phone;

    @Column(length = 150)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "professional_headline", length = 200)
    private String professionalHeadline;

    @Column(name = "professional_summary", columnDefinition = "TEXT")
    private String professionalSummary;

    @Column(name = "current_job_title", length = 150)
    private String currentJobTitle;

    @Column(name = "total_years_experience", precision = 4, scale = 1)
    private BigDecimal totalYearsExperience;

    @Column(name = "desired_job_title", length = 150)
    private String desiredJobTitle;

    @Column(name = "preferred_employment_type", length = 50)
    private String preferredEmploymentType;

    @Column(name = "preferred_work_arrangement", length = 30)
    private String preferredWorkArrangement;

    @Column(name = "preferred_job_location", length = 150)
    private String preferredJobLocation;

    @Column(name = "salary_expectation", length = 100)
    private String salaryExpectation;

    @Column(name = "availability_date")
    private LocalDate availabilityDate;

    @Column(name = "work_authorization_status", length = 100)
    private String workAuthorizationStatus;

    @Column(name = "sponsorship_required")
    private Boolean sponsorshipRequired;

    @Column(name = "security_clearance_status", length = 100)
    private String securityClearanceStatus;

    @Column(name = "profile_visibility", nullable = false)
    private Boolean profileVisibility = true;

    public CandidateProfile() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfessionalHeadline() {
        return professionalHeadline;
    }

    public void setProfessionalHeadline(String professionalHeadline) {
        this.professionalHeadline = professionalHeadline;
    }

    public String getProfessionalSummary() {
        return professionalSummary;
    }

    public void setProfessionalSummary(String professionalSummary) {
        this.professionalSummary = professionalSummary;
    }

    public String getCurrentJobTitle() {
        return currentJobTitle;
    }

    public void setCurrentJobTitle(String currentJobTitle) {
        this.currentJobTitle = currentJobTitle;
    }

    public BigDecimal getTotalYearsExperience() {
        return totalYearsExperience;
    }

    public void setTotalYearsExperience(BigDecimal totalYearsExperience) {
        this.totalYearsExperience = totalYearsExperience;
    }

    public String getDesiredJobTitle() {
        return desiredJobTitle;
    }

    public void setDesiredJobTitle(String desiredJobTitle) {
        this.desiredJobTitle = desiredJobTitle;
    }

    public String getPreferredEmploymentType() {
        return preferredEmploymentType;
    }

    public void setPreferredEmploymentType(String preferredEmploymentType) {
        this.preferredEmploymentType = preferredEmploymentType;
    }

    public String getPreferredWorkArrangement() {
        return preferredWorkArrangement;
    }

    public void setPreferredWorkArrangement(String preferredWorkArrangement) {
        this.preferredWorkArrangement = preferredWorkArrangement;
    }

    public String getPreferredJobLocation() {
        return preferredJobLocation;
    }

    public void setPreferredJobLocation(String preferredJobLocation) {
        this.preferredJobLocation = preferredJobLocation;
    }

    public String getSalaryExpectation() {
        return salaryExpectation;
    }

    public void setSalaryExpectation(String salaryExpectation) {
        this.salaryExpectation = salaryExpectation;
    }

    public LocalDate getAvailabilityDate() {
        return availabilityDate;
    }

    public void setAvailabilityDate(LocalDate availabilityDate) {
        this.availabilityDate = availabilityDate;
    }

    public String getWorkAuthorizationStatus() {
        return workAuthorizationStatus;
    }

    public void setWorkAuthorizationStatus(String workAuthorizationStatus) {
        this.workAuthorizationStatus = workAuthorizationStatus;
    }

    public Boolean getSponsorshipRequired() {
        return sponsorshipRequired;
    }

    public void setSponsorshipRequired(Boolean sponsorshipRequired) {
        this.sponsorshipRequired = sponsorshipRequired;
    }

    public String getSecurityClearanceStatus() {
        return securityClearanceStatus;
    }

    public void setSecurityClearanceStatus(String securityClearanceStatus) {
        this.securityClearanceStatus = securityClearanceStatus;
    }

    public Boolean getProfileVisibility() {
        return profileVisibility;
    }

    public void setProfileVisibility(Boolean profileVisibility) {
        this.profileVisibility = profileVisibility;
    }
}