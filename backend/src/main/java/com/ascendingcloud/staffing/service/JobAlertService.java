package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.JobAlertDTO;
import com.ascendingcloud.staffing.entity.JobAlert;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.EmploymentTypeRepository;
import com.ascendingcloud.staffing.repository.ExperienceLevelRepository;
import com.ascendingcloud.staffing.repository.JobAlertRepository;
import com.ascendingcloud.staffing.repository.JobCategoryRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobAlertService {

    private final JobAlertRepository jobAlertRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final EmploymentTypeRepository employmentTypeRepository;
    private final ExperienceLevelRepository experienceLevelRepository;

    public JobAlertService(
            JobAlertRepository jobAlertRepository,
            JobCategoryRepository jobCategoryRepository,
            EmploymentTypeRepository employmentTypeRepository,
            ExperienceLevelRepository experienceLevelRepository) {
        this.jobAlertRepository = jobAlertRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.employmentTypeRepository = employmentTypeRepository;
        this.experienceLevelRepository = experienceLevelRepository;
    }

    @Transactional(readOnly = true)
    public List<JobAlertDTO> getMyAlerts(User user) {
        return jobAlertRepository.findByUserId(user.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public JobAlertDTO getAlert(User user, Integer id) {
        return toDTO(findOwned(user, id));
    }

    @Transactional
    public JobAlertDTO createAlert(User user, JobAlertDTO dto) {
        JobAlert alert = new JobAlert();
        alert.setUser(user);

        apply(alert, dto);

        return toDTO(jobAlertRepository.save(alert));
    }

    @Transactional
    public JobAlertDTO updateAlert(User user, Integer id, JobAlertDTO dto) {
        JobAlert alert = findOwned(user, id);

        apply(alert, dto);

        return toDTO(jobAlertRepository.save(alert));
    }

    @Transactional
    public void deleteAlert(User user, Integer id) {
        jobAlertRepository.delete(findOwned(user, id));
    }

    private JobAlert findOwned(User user, Integer id) {
        JobAlert alert = jobAlertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job alert not found"));

        if (!alert.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("This job alert belongs to another user");
        }

        return alert;
    }

    private void apply(JobAlert alert, JobAlertDTO dto) {
        if (dto == null) {
            throw new BadRequestException("Request body is required");
        }

        boolean empty = (dto.keyword() == null || dto.keyword().isBlank())
                && (dto.location() == null || dto.location().isBlank())
                && dto.jobCategoryId() == null
                && dto.employmentTypeId() == null
                && dto.experienceLevelId() == null;

        if (empty) {
            throw new BadRequestException("Provide at least one alert criterion");
        }

        alert.setKeyword(dto.keyword());
        alert.setLocation(dto.location());

        alert.setJobCategory(dto.jobCategoryId() == null ? null
                : jobCategoryRepository.findById(dto.jobCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Job category not found")));

        alert.setEmploymentType(dto.employmentTypeId() == null ? null
                : employmentTypeRepository.findById(dto.employmentTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employment type not found")));

        alert.setExperienceLevel(dto.experienceLevelId() == null ? null
                : experienceLevelRepository.findById(dto.experienceLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Experience level not found")));
    }

    private JobAlertDTO toDTO(JobAlert a) {
        return new JobAlertDTO(
                a.getId(),
                a.getUser().getId(),
                a.getKeyword(),
                a.getLocation(),
                a.getJobCategory() != null ? a.getJobCategory().getId() : null,
                a.getEmploymentType() != null ? a.getEmploymentType().getId() : null,
                a.getExperienceLevel() != null ? a.getExperienceLevel().getId() : null,
                a.getCreatedAt());
    }
}
