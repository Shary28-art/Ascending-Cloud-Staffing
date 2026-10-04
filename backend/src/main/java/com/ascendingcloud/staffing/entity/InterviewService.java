package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Interview;
import com.ascendingcloud.staffing.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;

    public InterviewService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    public Optional<Interview> getInterviewById(Integer id) {
        return interviewRepository.findById(id);
    }

    public Interview createInterview(Interview interview) {
        return interviewRepository.save(interview);
    }

    public Interview updateInterview(Integer id, Interview interviewDetails) {
        Optional<Interview> optionalInterview = interviewRepository.findById(id);

        if (optionalInterview.isEmpty()) {
            return null;
        }

        Interview interview = optionalInterview.get();

        interview.setInterviewDate(interviewDetails.getInterviewDate());
        interview.setInterviewType(interviewDetails.getInterviewType());
        interview.setMeetingLink(interviewDetails.getMeetingLink());
        interview.setNotes(interviewDetails.getNotes());

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