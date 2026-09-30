package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.JobAlert;
import com.ascendingcloud.staffing.repository.JobAlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobAlertService {

    private final JobAlertRepository jobAlertRepository;

    public JobAlertService(JobAlertRepository jobAlertRepository) {
        this.jobAlertRepository = jobAlertRepository;
    }

    public List<JobAlert> getAllAlerts() {
        return jobAlertRepository.findAll();
    }

    public JobAlert getAlertById(Integer id) {
        return jobAlertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job alert not found"));
    }

    public List<JobAlert> getAlertsByUserId(Integer userId) {
        return jobAlertRepository.findByUserId(userId);
    }

    public JobAlert createAlert(JobAlert jobAlert) {
        return jobAlertRepository.save(jobAlert);
    }

    public JobAlert updateAlert(Integer id, JobAlert jobAlert) {
        JobAlert existingAlert = getAlertById(id);

        existingAlert.setUser(jobAlert.getUser());
        existingAlert.setKeyword(jobAlert.getKeyword());
        existingAlert.setLocation(jobAlert.getLocation());
        existingAlert.setJobCategory(jobAlert.getJobCategory());
        existingAlert.setEmploymentType(jobAlert.getEmploymentType());
        existingAlert.setExperienceLevel(jobAlert.getExperienceLevel());
        existingAlert.setCreatedAt(jobAlert.getCreatedAt());

        return jobAlertRepository.save(existingAlert);
    }

    public void deleteAlert(Integer id) {
        if (!jobAlertRepository.existsById(id)) {
            throw new RuntimeException("Job alert not found");
        }

        jobAlertRepository.deleteById(id);
    }
}