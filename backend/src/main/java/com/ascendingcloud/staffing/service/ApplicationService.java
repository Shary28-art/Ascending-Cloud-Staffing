package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.ApplicationResponse;
import com.ascendingcloud.staffing.dto.ApplicationStatusHistoryResponse;
import com.ascendingcloud.staffing.entity.Application;
import com.ascendingcloud.staffing.entity.ApplicationStatus;
import com.ascendingcloud.staffing.entity.ApplicationStatusHistory;
import com.ascendingcloud.staffing.entity.CandidateProfile;
import com.ascendingcloud.staffing.entity.Job;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.ApplicationRepository;
import com.ascendingcloud.staffing.repository.ApplicationStatusHistoryRepository;
import com.ascendingcloud.staffing.repository.CandidateProfileRepository;
import com.ascendingcloud.staffing.repository.EmployerProfileRepository;
import com.ascendingcloud.staffing.repository.ResumeRepository;
import com.ascendingcloud.staffing.security.CurrentUserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationStatusHistoryRepository statusHistoryRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final ResumeRepository resumeRepository;
    private final EmployerProfileRepository employerProfileRepository;
    private final JobService jobService;
    private final NotificationService notificationService;
    private final CurrentUserService currentUserService;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            ApplicationStatusHistoryRepository statusHistoryRepository,
            CandidateProfileRepository candidateProfileRepository,
            ResumeRepository resumeRepository,
            EmployerProfileRepository employerProfileRepository,
            JobService jobService,
            NotificationService notificationService,
            CurrentUserService currentUserService) {
        this.applicationRepository = applicationRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.resumeRepository = resumeRepository;
        this.employerProfileRepository = employerProfileRepository;
        this.jobService = jobService;
        this.notificationService = notificationService;
        this.currentUserService = currentUserService;
    }

    /** A candidate applies to a public (approved, active) job. */
    @Transactional
    public ApplicationResponse apply(User user, Integer jobId) {

        if (jobId == null) {
            throw new BadRequestException("jobId is required");
        }

        CandidateProfile candidate = candidateProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Candidate profile not found. Complete your profile first"));

        Job job = jobService.find(jobId);

        if (!jobService.isPublic(job)) {
            throw new BadRequestException("This job is not open for applications");
        }

        if (resumeRepository.findByCandidateId(candidate.getId()).isEmpty()) {
            throw new BadRequestException("Upload your resume before applying");
        }

        if (applicationRepository.existsByJobIdAndCandidateId(jobId, candidate.getId())) {
            throw new BadRequestException("You have already applied for this job");
        }

        Application application = new Application();
        application.setJob(job);
        application.setCandidate(candidate);
        application.setStatus(ApplicationStatus.APPLIED.getLabel());
        application.setAppliedAt(LocalDateTime.now());

        Application saved = applicationRepository.save(application);

        saveHistory(saved, ApplicationStatus.APPLIED.getLabel());

        notificationService.notify(user,
                "Application Submitted",
                "Your application for " + job.getTitle() + " has been submitted successfully.");

        if (job.getCompany() != null) {
            employerProfileRepository.findByCompanyId(job.getCompany().getId())
                    .forEach(p -> notificationService.notify(p.getUser(),
                            "New application",
                            user.getName() + " applied for " + job.getTitle() + "."));
        }

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications(User user) {
        CandidateProfile candidate = candidateProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));

        return applicationRepository
                .findByCandidateIdOrderByAppliedAtDesc(candidate.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getJobApplications(User user, Integer jobId) {
        Job job = jobService.find(jobId);
        jobService.assertCanManage(user, job);

        return applicationRepository.findByJobIdOrderByAppliedAtDesc(jobId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /** Admin: everything. Employer: only their company's applications. */
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getAll(User user) {
        List<Application> list;

        if (currentUserService.isAdmin(user)) {
            list = applicationRepository.findAllByOrderByAppliedAtDesc();
        } else {
            Integer companyId = currentUserService.getEmployerCompanyId(user);
            list = companyId == null
                    ? List.of()
                    : applicationRepository.findByJobCompanyIdOrderByAppliedAtDesc(companyId);
        }

        return list.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ApplicationResponse getById(User user, Integer id) {
        Application application = find(id);
        assertCanView(user, application);

        return toResponse(application);
    }

    @Transactional
    public ApplicationResponse updateStatus(User user, Integer applicationId, String statusValue) {

        Application application = find(applicationId);
        ApplicationStatus newStatus = ApplicationStatus.fromLabel(statusValue);
        ApplicationStatus current = ApplicationStatus.find(application.getStatus())
                .orElse(ApplicationStatus.APPLIED);

        if (currentUserService.isCandidate(user)) {
            if (!application.getCandidate().getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("You cannot modify this application");
            }

            if (newStatus != ApplicationStatus.WITHDRAWN) {
                throw new AccessDeniedException("Candidates can only withdraw an application");
            }
        } else {
            jobService.assertCanManage(user, application.getJob());
        }

        if (current == newStatus) {
            throw new BadRequestException("Application is already " + newStatus.getLabel());
        }

        if (current.isTerminal() && !currentUserService.isAdmin(user)) {
            throw new BadRequestException(
                    "This application is already " + current.getLabel()
                            + " and can no longer be changed");
        }

        application.setStatus(newStatus.getLabel());

        Application saved = applicationRepository.save(application);

        saveHistory(saved, newStatus.getLabel());

        // Tell the candidate (unless they made the change themselves)
        if (!currentUserService.isCandidate(user)) {
            notificationService.notify(saved.getCandidate().getUser(),
                    "Application Status Updated",
                    "Your application for " + saved.getJob().getTitle()
                            + " is now: " + newStatus.getLabel());
        }

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ApplicationStatusHistoryResponse> getStatusHistory(User user, Integer applicationId) {
        Application application = find(applicationId);
        assertCanView(user, application);

        return statusHistoryRepository
                .findByApplicationIdOrderByChangedAtAsc(applicationId)
                .stream()
                .map(h -> new ApplicationStatusHistoryResponse(
                        h.getId(),
                        applicationId,
                        h.getStatus(),
                        h.getChangedAt()))
                .toList();
    }

    @Transactional
    public void delete(Integer id) {
        if (!applicationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Application not found");
        }

        applicationRepository.deleteById(id);
    }

    /** Valid status labels for dropdowns. */
    public List<String> getAllowedStatuses() {
        return ApplicationStatus.labels();
    }

    // --------------------------------------------------------------- helpers

    public Application find(Integer id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }

    /** The owning candidate, the job's employer, or an admin. */
    public void assertCanView(User user, Application application) {
        if (currentUserService.isCandidate(user)) {
            if (application.getCandidate().getUser().getId().equals(user.getId())) {
                return;
            }
            throw new AccessDeniedException("You cannot access this application");
        }

        jobService.assertCanManage(user, application.getJob());
    }

    private void saveHistory(Application application, String status) {
        ApplicationStatusHistory history = new ApplicationStatusHistory();
        history.setApplication(application);
        history.setStatus(status);
        history.setChangedAt(LocalDateTime.now());

        statusHistoryRepository.save(history);
    }

    private ApplicationResponse toResponse(Application a) {
        User candidateUser = a.getCandidate() != null ? a.getCandidate().getUser() : null;

        return new ApplicationResponse(
                a.getId(),
                a.getJob().getId(),
                a.getJob().getTitle(),
                a.getCandidate().getId(),
                candidateUser != null ? candidateUser.getName() : null,
                a.getStatus(),
                a.getAppliedAt());
    }
}
