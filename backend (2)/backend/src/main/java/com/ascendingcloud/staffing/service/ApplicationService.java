package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.ApplicationResponse;
import com.ascendingcloud.staffing.dto.ApplicationStatusHistoryResponse;
import com.ascendingcloud.staffing.entity.Application;
import com.ascendingcloud.staffing.entity.ApplicationStatusHistory;
import com.ascendingcloud.staffing.entity.CandidateProfile;
import com.ascendingcloud.staffing.entity.Job;
import com.ascendingcloud.staffing.entity.Notification;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.repository.ApplicationRepository;
import com.ascendingcloud.staffing.repository.ApplicationStatusHistoryRepository;
import com.ascendingcloud.staffing.repository.CandidateProfileRepository;
import com.ascendingcloud.staffing.repository.JobRepository;
import com.ascendingcloud.staffing.repository.NotificationRepository;
import com.ascendingcloud.staffing.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationStatusHistoryRepository statusHistoryRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            ApplicationStatusHistoryRepository statusHistoryRepository,
            CandidateProfileRepository candidateProfileRepository,
            JobRepository jobRepository,
            UserRepository userRepository,
            NotificationRepository notificationRepository
    ) {
        this.applicationRepository = applicationRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public ApplicationResponse apply(String email, Integer jobId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CandidateProfile candidate = candidateProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Candidate profile not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (applicationRepository.existsByJobIdAndCandidateId(
                jobId,
                candidate.getId()
        )) {
            throw new RuntimeException("You have already applied for this job");
        }

        Application application = new Application();

        application.setJob(job);
        application.setCandidate(candidate);
        application.setStatus("Applied");
        application.setAppliedAt(LocalDateTime.now());

        Application savedApplication = applicationRepository.save(application);

        saveStatusHistory(savedApplication, "Applied");

        createNotification(
                user,
                "Application Submitted",
                "Your application for " + job.getTitle() + " has been submitted successfully."
        );

        return toResponse(savedApplication);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CandidateProfile candidate = candidateProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Candidate profile not found"));

        return applicationRepository
                .findByCandidateIdOrderByAppliedAtDesc(candidate.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getJobApplications(
            String email,
            Integer jobId
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"EMPLOYER".equalsIgnoreCase(user.getRole())
                && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Only employers or admins can view applications");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if ("EMPLOYER".equalsIgnoreCase(user.getRole())) {
            boolean ownsJob = job.getCompany() != null &&
                    job.getCompany().getId() != null;

            if (!ownsJob) {
                throw new RuntimeException("Job is not linked to a company");
            }
        }

        return applicationRepository
                .findByJobIdOrderByAppliedAtDesc(jobId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ApplicationResponse updateStatus(
            String email,
            Integer applicationId,
            String newStatus
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!isAllowedStatus(newStatus)) {
            throw new RuntimeException("Invalid application status");
        }

        if ("CANDIDATE".equalsIgnoreCase(user.getRole())) {

            Integer candidateUserId =
                    application.getCandidate()
                            .getUser()
                            .getId();

            if (!candidateUserId.equals(user.getId())) {
                throw new RuntimeException("You cannot modify this application");
            }

            if (!"Withdrawn".equalsIgnoreCase(newStatus)) {
                throw new RuntimeException(
                        "Candidates can only withdraw an application"
                );
            }
        }

        application.setStatus(newStatus);

        Application savedApplication =
                applicationRepository.save(application);

        saveStatusHistory(savedApplication, newStatus);

        User candidateUser =
                savedApplication.getCandidate().getUser();

        createNotification(
                candidateUser,
                "Application Status Updated",
                "Your application for "
                        + savedApplication.getJob().getTitle()
                        + " is now: "
                        + newStatus
        );

        return toResponse(savedApplication);
    }

    @Transactional(readOnly = true)
    public List<ApplicationStatusHistoryResponse> getStatusHistory(
            Integer applicationId
    ) {

        if (!applicationRepository.existsById(applicationId)) {
            throw new RuntimeException("Application not found");
        }

        return statusHistoryRepository
                .findByApplicationIdOrderByChangedAtAsc(applicationId)
                .stream()
                .map(history -> new ApplicationStatusHistoryResponse(
                        history.getId(),
                        history.getApplication().getId(),
                        history.getStatus(),
                        history.getChangedAt()
                ))
                .toList();
    }

    private void saveStatusHistory(
            Application application,
            String status
    ) {

        ApplicationStatusHistory history =
                new ApplicationStatusHistory();

        history.setApplication(application);
        history.setStatus(status);
        history.setChangedAt(LocalDateTime.now());

        statusHistoryRepository.save(history);
    }

    private void createNotification(
            User user,
            String title,
            String message
    ) {

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    private ApplicationResponse toResponse(Application application) {

        String candidateName = null;

        if (application.getCandidate() != null
                && application.getCandidate().getUser() != null) {

            candidateName =
                    application.getCandidate()
                            .getUser()
                            .getName();
        }

        return new ApplicationResponse(
                application.getId(),
                application.getJob().getId(),
                application.getJob().getTitle(),
                application.getCandidate().getId(),
                candidateName,
                application.getStatus(),
                application.getAppliedAt()
        );
    }

    private boolean isAllowedStatus(String status) {

        return status.equalsIgnoreCase("Applied")
                || status.equalsIgnoreCase("Under Review")
                || status.equalsIgnoreCase("Shortlisted")
                || status.equalsIgnoreCase("Interview Scheduled")
                || status.equalsIgnoreCase("Selected/Hired")
                || status.equalsIgnoreCase("Rejected")
                || status.equalsIgnoreCase("Withdrawn")
                || status.equalsIgnoreCase("Screening")
                || status.equalsIgnoreCase("Submitted to Employer")
                || status.equalsIgnoreCase("Interview Completed")
                || status.equalsIgnoreCase("Offer Extended")
                || status.equalsIgnoreCase("Offer Accepted")
                || status.equalsIgnoreCase("Hired/Placed")
                || status.equalsIgnoreCase("On Hold")
                || status.equalsIgnoreCase("Offer Declined")
                || status.equalsIgnoreCase("Job Closed");
    }
}