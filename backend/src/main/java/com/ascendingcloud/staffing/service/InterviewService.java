package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.InterviewDTO;
import com.ascendingcloud.staffing.entity.Application;
import com.ascendingcloud.staffing.entity.Interview;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.InterviewRepository;
import com.ascendingcloud.staffing.security.CurrentUserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationService applicationService;
    private final JobService jobService;
    private final NotificationService notificationService;
    private final CurrentUserService currentUserService;

    public InterviewService(
            InterviewRepository interviewRepository,
            ApplicationService applicationService,
            JobService jobService,
            NotificationService notificationService,
            CurrentUserService currentUserService) {
        this.interviewRepository = interviewRepository;
        this.applicationService = applicationService;
        this.jobService = jobService;
        this.notificationService = notificationService;
        this.currentUserService = currentUserService;
    }

    /** Candidates see their own, employers their company's, admins all. */
    @Transactional(readOnly = true)
    public List<InterviewDTO> getInterviews(User user) {
        List<Interview> list;

        if (currentUserService.isAdmin(user)) {
            list = interviewRepository.findAllByOrderByScheduledAtAsc();
        } else if (currentUserService.isEmployer(user)) {
            Integer companyId = currentUserService.getEmployerCompanyId(user);
            list = companyId == null
                    ? List.of()
                    : interviewRepository.findByApplicationJobCompanyIdOrderByScheduledAtAsc(companyId);
        } else {
            list = interviewRepository
                    .findByApplicationCandidateUserIdOrderByScheduledAtAsc(user.getId());
        }

        return list.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public InterviewDTO getInterview(User user, Integer id) {
        Interview interview = find(id);
        applicationService.assertCanView(user, interview.getApplication());

        return toDTO(interview);
    }

    @Transactional
    public InterviewDTO createInterview(User user, InterviewDTO dto) {
        if (dto == null || dto.applicationId() == null) {
            throw new BadRequestException("applicationId is required");
        }

        if (dto.scheduledAt() == null) {
            throw new BadRequestException("scheduledAt is required");
        }

        Application application = applicationService.find(dto.applicationId());
        jobService.assertCanManage(user, application.getJob());

        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setScheduledAt(dto.scheduledAt());
        interview.setInterviewType(dto.interviewType());
        interview.setMeetingLink(dto.meetingLink());
        interview.setNotes(dto.notes());
        interview.setStatus(dto.status() == null ? "Scheduled" : dto.status());

        Interview saved = interviewRepository.save(interview);

        notificationService.notify(application.getCandidate().getUser(),
                "Interview scheduled",
                "An interview for " + application.getJob().getTitle()
                        + " is scheduled for " + saved.getScheduledAt() + ".");

        return toDTO(saved);
    }

    @Transactional
    public InterviewDTO updateInterview(User user, Integer id, InterviewDTO dto) {
        Interview interview = find(id);
        jobService.assertCanManage(user, interview.getApplication().getJob());

        if (dto.scheduledAt() != null) interview.setScheduledAt(dto.scheduledAt());
        if (dto.interviewType() != null) interview.setInterviewType(dto.interviewType());
        if (dto.status() != null) interview.setStatus(dto.status());
        if (dto.meetingLink() != null) interview.setMeetingLink(dto.meetingLink());
        if (dto.notes() != null) interview.setNotes(dto.notes());

        Interview saved = interviewRepository.save(interview);

        notificationService.notify(saved.getApplication().getCandidate().getUser(),
                "Interview updated",
                "Your interview for " + saved.getApplication().getJob().getTitle()
                        + " was updated.");

        return toDTO(saved);
    }

    @Transactional
    public void deleteInterview(User user, Integer id) {
        Interview interview = find(id);

        if (!currentUserService.isAdmin(user)) {
            try {
                jobService.assertCanManage(user, interview.getApplication().getJob());
            } catch (AccessDeniedException ex) {
                throw new AccessDeniedException("You cannot delete this interview");
            }
        }

        interviewRepository.delete(interview);
    }

    private Interview find(Integer id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
    }

    private InterviewDTO toDTO(Interview i) {
        return new InterviewDTO(
                i.getId(),
                i.getApplication().getId(),
                i.getScheduledAt(),
                i.getInterviewType(),
                i.getStatus(),
                i.getMeetingLink(),
                i.getNotes());
    }
}
