package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Interview;
import com.ascendingcloud.staffing.repository.InterviewRepository;
import org.springframework.stereotype.Service;

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
        return interviewRepository.findById(id).orElse(null);
    }

    public Interview createInterview(Interview interview) {
        return interviewRepository.save(interview);
    }

    public Interview updateInterview(
            Integer id,
            Interview details) {

        Interview interview =
                interviewRepository.findById(id).orElse(null);

        if (interview == null) {
            return null;
        }

        if (details.getApplication() != null) {
            interview.setApplication(details.getApplication());
        }

        interview.setInterviewDate(details.getInterviewDate());
        interview.setInterviewType(details.getInterviewType());
        interview.setMeetingLink(details.getMeetingLink());
        interview.setNotes(details.getNotes());

        return interviewRepository.save(interview);
    }

    public boolean deleteInterview(Integer id) {
        if (!interviewRepository.existsById(id)) {
            return false;
        }

        interviewRepository.deleteById(id);
        return true;
    }
}