package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.JobDTO;
import com.ascendingcloud.staffing.entity.ApprovalStatus;
import com.ascendingcloud.staffing.entity.Company;
import com.ascendingcloud.staffing.entity.Job;
import com.ascendingcloud.staffing.entity.JobStatus;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.mapper.JobMapper;
import com.ascendingcloud.staffing.repository.*;
import com.ascendingcloud.staffing.security.CurrentUserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Job postings. Rule from the requirements: every job must be approved by an
 * admin before it appears publicly. A job is public only when
 * approvalStatus = APPROVED, status = ACTIVE and the deadline has not passed.
 */
@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final CloudPlatformRepository cloudPlatformRepository;
    private final EmploymentTypeRepository employmentTypeRepository;
    private final ExperienceLevelRepository experienceLevelRepository;
    private final WorkplaceTypeRepository workplaceTypeRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final CurrentUserService currentUserService;
    private final NotificationService notificationService;
    private final EmployerProfileRepository employerProfileRepository;

    public JobService(
            JobRepository jobRepository,
            CompanyRepository companyRepository,
            CloudPlatformRepository cloudPlatformRepository,
            EmploymentTypeRepository employmentTypeRepository,
            ExperienceLevelRepository experienceLevelRepository,
            WorkplaceTypeRepository workplaceTypeRepository,
            JobCategoryRepository jobCategoryRepository,
            CurrentUserService currentUserService,
            NotificationService notificationService,
            EmployerProfileRepository employerProfileRepository) {

        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.cloudPlatformRepository = cloudPlatformRepository;
        this.employmentTypeRepository = employmentTypeRepository;
        this.experienceLevelRepository = experienceLevelRepository;
        this.workplaceTypeRepository = workplaceTypeRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.currentUserService = currentUserService;
        this.notificationService = notificationService;
        this.employerProfileRepository = employerProfileRepository;
    }

    // ---------------------------------------------------------------- public

    @Transactional(readOnly = true)
    public List<JobDTO> searchPublicJobs(String title, String location) {
        return jobRepository.searchPublic(
                        ApprovalStatus.APPROVED.name(),
                        JobStatus.ACTIVE.name(),
                        LocalDate.now(),
                        title == null ? "" : title.trim(),
                        location == null ? "" : location.trim())
                .stream()
                .map(JobMapper::toDTO)
                .toList();
    }

    /** Public jobs for anyone; unapproved jobs only for the owner or an admin. */
    @Transactional(readOnly = true)
    public JobDTO getJob(Integer id) {
        Job job = find(id);

        if (isPublic(job)) {
            return JobMapper.toDTO(job);
        }

        User user = currentUserService.getUserOrNull();

        if (user != null && canManage(user, job)) {
            return JobMapper.toDTO(job);
        }

        // Do not reveal that an unapproved job exists
        throw new ResourceNotFoundException("Job not found");
    }

    /** True when a candidate may see and apply to the job. */
    public boolean isPublic(Job job) {
        return ApprovalStatus.APPROVED.name().equals(job.getApprovalStatus())
                && JobStatus.ACTIVE.name().equals(job.getStatus())
                && (job.getApplicationDeadline() == null
                || !job.getApplicationDeadline().isBefore(LocalDate.now()));
    }

    // -------------------------------------------------------------- employer

    @Transactional(readOnly = true)
    public List<JobDTO> getMyCompanyJobs(User user) {
        Integer companyId = currentUserService.getEmployerCompanyId(user);

        if (companyId == null) {
            return List.of();
        }

        return jobRepository.findByCompanyIdOrderByCreatedAtDesc(companyId)
                .stream()
                .map(JobMapper::toDTO)
                .toList();
    }

    @Transactional
    public JobDTO createJob(User user, JobDTO dto) {
        validate(dto);

        Job job = new Job();
        apply(job, dto);

        if (currentUserService.isAdmin(user)) {
            if (dto.companyId() == null) {
                throw new BadRequestException("companyId is required");
            }

            job.setCompany(findCompany(dto.companyId()));
            job.setApprovalStatus(ApprovalStatus.APPROVED.name());
            job.setApprovedAt(LocalDateTime.now());
            job.setApprovedByUserId(user.getId());
        } else {
            Integer companyId = currentUserService.getEmployerCompanyId(user);

            if (companyId == null) {
                throw new BadRequestException(
                        "Create or link a company in your employer profile before posting jobs");
            }

            // Employers can only post for their own company
            job.setCompany(findCompany(companyId));
            job.setApprovalStatus(ApprovalStatus.PENDING.name());
        }

        job.setStatus(parseStatus(dto.status(), JobStatus.DRAFT));

        return JobMapper.toDTO(jobRepository.save(job));
    }

    @Transactional
    public JobDTO updateJob(User user, Integer id, JobDTO dto) {
        Job job = find(id);
        assertCanManage(user, job);
        validate(dto);

        apply(job, dto);

        if (dto.status() != null) {
            job.setStatus(parseStatus(dto.status(), JobStatus.DRAFT));
        }

        // Edits by an employer must be re-approved before they go public again.
        if (!currentUserService.isAdmin(user)) {
            job.setApprovalStatus(ApprovalStatus.PENDING.name());
            job.setRejectionReason(null);
            job.setApprovedAt(null);
            job.setApprovedByUserId(null);
        } else if (dto.companyId() != null) {
            job.setCompany(findCompany(dto.companyId()));
        }

        return JobMapper.toDTO(jobRepository.save(job));
    }

    /** Pause, close, mark filled, etc. Does not need re-approval. */
    @Transactional
    public JobDTO updateJobStatus(User user, Integer id, String statusValue) {
        Job job = find(id);
        assertCanManage(user, job);

        job.setStatus(JobStatus.parse(statusValue).name());

        return JobMapper.toDTO(jobRepository.save(job));
    }

    @Transactional
    public void deleteJob(User user, Integer id) {
        Job job = find(id);
        assertCanManage(user, job);

        jobRepository.delete(job);
    }

    // ----------------------------------------------------------------- admin

    @Transactional(readOnly = true)
    public List<JobDTO> getAllJobsForAdmin(String approvalStatus) {
        List<Job> jobs = (approvalStatus == null || approvalStatus.isBlank())
                ? jobRepository.findAllByOrderByCreatedAtDesc()
                : jobRepository.findByApprovalStatusOrderByCreatedAtDesc(
                parseApproval(approvalStatus).name());

        return jobs.stream().map(JobMapper::toDTO).toList();
    }

    @Transactional
    public JobDTO approveJob(User admin, Integer id) {
        Job job = find(id);

        job.setApprovalStatus(ApprovalStatus.APPROVED.name());
        job.setRejectionReason(null);
        job.setApprovedAt(LocalDateTime.now());
        job.setApprovedByUserId(admin.getId());

        // A draft that gets approved becomes active so it actually appears.
        if (JobStatus.DRAFT.name().equals(job.getStatus())) {
            job.setStatus(JobStatus.ACTIVE.name());
        }

        Job saved = jobRepository.save(job);

        notifyEmployers(saved,
                "Job approved",
                "Your job \"" + saved.getTitle() + "\" was approved and is now visible to candidates.");

        return JobMapper.toDTO(saved);
    }

    @Transactional
    public JobDTO rejectJob(User admin, Integer id, String reason) {
        Job job = find(id);

        job.setApprovalStatus(ApprovalStatus.REJECTED.name());
        job.setRejectionReason(reason);
        job.setApprovedAt(null);
        job.setApprovedByUserId(admin.getId());

        Job saved = jobRepository.save(job);

        notifyEmployers(saved,
                "Job rejected",
                "Your job \"" + saved.getTitle() + "\" was not approved"
                        + (reason == null || reason.isBlank() ? "." : ": " + reason));

        return JobMapper.toDTO(saved);
    }

    // --------------------------------------------------------------- helpers

    public Job find(Integer id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
    }

    /** True for admins and for employers of the job's company. */
    public boolean canManage(User user, Job job) {
        if (currentUserService.isAdmin(user)) {
            return true;
        }

        if (!currentUserService.isEmployer(user) || job.getCompany() == null) {
            return false;
        }

        Integer companyId = currentUserService.getEmployerCompanyId(user);

        return companyId != null && companyId.equals(job.getCompany().getId());
    }

    public void assertCanManage(User user, Job job) {
        if (!canManage(user, job)) {
            throw new AccessDeniedException("You do not have access to this job");
        }
    }

    private void notifyEmployers(Job job, String title, String message) {
        if (job.getCompany() == null) {
            return;
        }

        employerProfileRepository.findByCompanyId(job.getCompany().getId())
                .forEach(profile -> notificationService.notify(profile.getUser(), title, message));
    }

    private void validate(JobDTO dto) {
        if (dto == null) {
            throw new BadRequestException("Request body is required");
        }

        if (dto.title() == null || dto.title().isBlank()) {
            throw new BadRequestException("Job title is required");
        }

        if (dto.description() == null || dto.description().isBlank()) {
            throw new BadRequestException("Job description is required");
        }

        if (dto.numberOfPositions() != null && dto.numberOfPositions() < 1) {
            throw new BadRequestException("Number of positions must be at least 1");
        }
    }

    private void apply(Job job, JobDTO dto) {
        job.setTitle(dto.title().trim());
        job.setLocation(dto.location());
        job.setSalary(dto.salary());
        job.setDescription(dto.description());
        job.setResponsibilities(dto.responsibilities());
        job.setRequiredQualifications(dto.requiredQualifications());
        job.setPreferredSkills(dto.preferredSkills());
        job.setEducationRequirements(dto.educationRequirements());
        job.setCertificationRequirements(dto.certificationRequirements());
        job.setNumberOfPositions(dto.numberOfPositions());
        job.setApplicationDeadline(dto.applicationDeadline());
        job.setExpectedStartDate(dto.expectedStartDate());
        job.setWorkAuthorization(dto.workAuthorization());
        job.setSponsorshipAvailable(dto.sponsorshipAvailable());
        job.setSecurityClearanceRequired(dto.securityClearanceRequired());
        job.setTravelRequirements(dto.travelRequirements());
        job.setScreeningQuestions(dto.screeningQuestions());
        job.setAssignedRecruiterId(dto.assignedRecruiterId());

        job.setCloudPlatform(dto.cloudPlatformId() == null ? null
                : cloudPlatformRepository.findById(dto.cloudPlatformId())
                .orElseThrow(() -> new ResourceNotFoundException("Cloud platform not found")));

        job.setEmploymentType(dto.employmentTypeId() == null ? null
                : employmentTypeRepository.findById(dto.employmentTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employment type not found")));

        job.setExperienceLevel(dto.experienceLevelId() == null ? null
                : experienceLevelRepository.findById(dto.experienceLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Experience level not found")));

        job.setWorkplaceType(dto.workplaceTypeId() == null ? null
                : workplaceTypeRepository.findById(dto.workplaceTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Workplace type not found")));

        job.setJobCategory(dto.jobCategoryId() == null ? null
                : jobCategoryRepository.findById(dto.jobCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Job category not found")));
    }

    private Company findCompany(Integer companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
    }

    private String parseStatus(String value, JobStatus fallback) {
        return (value == null || value.isBlank())
                ? fallback.name()
                : JobStatus.parse(value).name();
    }

    private ApprovalStatus parseApproval(String value) {
        try {
            return ApprovalStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid approval status: " + value);
        }
    }
}
