package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Interview;
import com.ascendingcloud.staffing.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;

    public InterviewService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    public Interview getInterviewById(Integer id) {
        return interviewRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Interview not found"));
    }

    public Interview createInterview(Interview interview) {

        if (interview.getCreatedAt() == null) {
            interview.setCreatedAt(LocalDateTime.now());
        }

        return interviewRepository.save(interview);
    }

    public Interview updateInterview(
            Integer id,
            Interview interview) {

        Interview existing = getInterviewById(id);

        existing.setApplication(interview.getApplication());
        existing.setInterviewDate(interview.getInterviewDate());
        existing.setInterviewType(interview.getInterviewType());
        existing.setMeetingLink(interview.getMeetingLink());
        existing.setNotes(interview.getNotes());

        return interviewRepository.save(existing);
    }

    public void deleteInterview(Integer id) {

        if (!interviewRepository.existsById(id)) {
            throw new RuntimeException("Interview not found");
        }

        interviewRepository.deleteById(id);
    }
}