package com.ascendingcloud.staffing.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String title;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(length = 150)
    private String location;

    @Column(length = 50)
    private String salary;

    @ManyToOne
    @JoinColumn(name = "cloud_platform_id")
    private CloudPlatform cloudPlatform;

    @ManyToOne
    @JoinColumn(name = "employment_type_id")
    private EmploymentType employmentType;

    @ManyToOne
    @JoinColumn(name = "experience_level_id")
    private ExperienceLevel experienceLevel;

    @ManyToOne
    @JoinColumn(name = "workplace_type_id")
    private WorkplaceType workplaceType;

    @ManyToOne
    @JoinColumn(name = "job_category_id")
    private JobCategory jobCategory;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(name = "required_qualifications", columnDefinition = "TEXT")
    private String requiredQualifications;

    @Column(name = "preferred_skills", columnDefinition = "TEXT")
    private String preferredSkills;

    @Column(name = "education_requirements", length = 255)
    private String educationRequirements;

    @Column(name = "certification_requirements", length = 255)
    private String certificationRequirements;

    @Column(name = "number_of_positions")
    private Integer numberOfPositions;

    @Column(name = "application_deadline")
    private LocalDate applicationDeadline;

    @Column(name = "expected_start_date")
    private LocalDate expectedStartDate;

    @Column(name = "work_authorization", length = 150)
    private String workAuthorization;

    @Column(name = "sponsorship_available")
    private Boolean sponsorshipAvailable;

    @Column(name = "security_clearance_required", length = 100)
    private String securityClearanceRequired;

    @Column(name = "travel_requirements", length = 150)
    private String travelRequirements;

    @Column(name = "screening_questions", columnDefinition = "TEXT")
    private String screeningQuestions;

    @Column(name = "assigned_recruiter_id")
    private Integer assignedRecruiterId;

    @Column(length = 20)
    private String status;

    @Column(name = "approval_status", length = 20)
    private String approvalStatus;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "approved_by_user_id")
    private Integer approvedByUserId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Job() {
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = JobStatus.DRAFT.name();
        }
        if (approvalStatus == null) {
            approvalStatus = ApprovalStatus.PENDING.name();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public CloudPlatform getCloudPlatform() {
        return cloudPlatform;
    }

    public void setCloudPlatform(CloudPlatform cloudPlatform) {
        this.cloudPlatform = cloudPlatform;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public ExperienceLevel getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(ExperienceLevel experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public WorkplaceType getWorkplaceType() {
        return workplaceType;
    }

    public void setWorkplaceType(WorkplaceType workplaceType) {
        this.workplaceType = workplaceType;
    }

    public JobCategory getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(JobCategory jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public String getRequiredQualifications() {
        return requiredQualifications;
    }

    public void setRequiredQualifications(String requiredQualifications) {
        this.requiredQualifications = requiredQualifications;
    }

    public String getPreferredSkills() {
        return preferredSkills;
    }

    public void setPreferredSkills(String preferredSkills) {
        this.preferredSkills = preferredSkills;
    }

    public String getEducationRequirements() {
        return educationRequirements;
    }

    public void setEducationRequirements(String educationRequirements) {
        this.educationRequirements = educationRequirements;
    }

    public String getCertificationRequirements() {
        return certificationRequirements;
    }

    public void setCertificationRequirements(String certificationRequirements) {
        this.certificationRequirements = certificationRequirements;
    }

    public Integer getNumberOfPositions() {
        return numberOfPositions;
    }

    public void setNumberOfPositions(Integer numberOfPositions) {
        this.numberOfPositions = numberOfPositions;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public LocalDate getExpectedStartDate() {
        return expectedStartDate;
    }

    public void setExpectedStartDate(LocalDate expectedStartDate) {
        this.expectedStartDate = expectedStartDate;
    }

    public String getWorkAuthorization() {
        return workAuthorization;
    }

    public void setWorkAuthorization(String workAuthorization) {
        this.workAuthorization = workAuthorization;
    }

    public Boolean getSponsorshipAvailable() {
        return sponsorshipAvailable;
    }

    public void setSponsorshipAvailable(Boolean sponsorshipAvailable) {
        this.sponsorshipAvailable = sponsorshipAvailable;
    }

    public String getSecurityClearanceRequired() {
        return securityClearanceRequired;
    }

    public void setSecurityClearanceRequired(String securityClearanceRequired) {
        this.securityClearanceRequired = securityClearanceRequired;
    }

    public String getTravelRequirements() {
        return travelRequirements;
    }

    public void setTravelRequirements(String travelRequirements) {
        this.travelRequirements = travelRequirements;
    }

    public String getScreeningQuestions() {
        return screeningQuestions;
    }

    public void setScreeningQuestions(String screeningQuestions) {
        this.screeningQuestions = screeningQuestions;
    }

    public Integer getAssignedRecruiterId() {
        return assignedRecruiterId;
    }

    public void setAssignedRecruiterId(Integer assignedRecruiterId) {
        this.assignedRecruiterId = assignedRecruiterId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Integer getApprovedByUserId() {
        return approvedByUserId;
    }

    public void setApprovedByUserId(Integer approvedByUserId) {
        this.approvedByUserId = approvedByUserId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
