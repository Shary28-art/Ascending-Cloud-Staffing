package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.*;
import com.ascendingcloud.staffing.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final CloudPlatformRepository cloudPlatformRepository;
    private final EmploymentTypeRepository employmentTypeRepository;
    private final ExperienceLevelRepository experienceLevelRepository;
    private final WorkplaceTypeRepository workplaceTypeRepository;

    public JobService(
            JobRepository jobRepository,
            CompanyRepository companyRepository,
            CloudPlatformRepository cloudPlatformRepository,
            EmploymentTypeRepository employmentTypeRepository,
            ExperienceLevelRepository experienceLevelRepository,
            WorkplaceTypeRepository workplaceTypeRepository) {

        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.cloudPlatformRepository = cloudPlatformRepository;
        this.employmentTypeRepository = employmentTypeRepository;
        this.experienceLevelRepository = experienceLevelRepository;
        this.workplaceTypeRepository = workplaceTypeRepository;
    }

    public Job createJob(
            String title,
            Integer companyId,
            String location,
            String salary,
            Integer cloudPlatformId,
            Integer employmentTypeId,
            Integer experienceLevelId,
            Integer workplaceTypeId,
            String description) {

        Job job = new Job();

        job.setTitle(title);
        job.setLocation(location);
        job.setSalary(salary);
        job.setDescription(description);

        if (companyId != null) {
            job.setCompany(
                    companyRepository.findById(companyId)
                            .orElseThrow(() ->
                                    new RuntimeException("Company not found"))
            );
        }

        if (cloudPlatformId != null) {
            job.setCloudPlatform(
                    cloudPlatformRepository.findById(cloudPlatformId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Cloud platform not found"))
            );
        }

        if (employmentTypeId != null) {
            job.setEmploymentType(
                    employmentTypeRepository.findById(employmentTypeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Employment type not found"))
            );
        }

        if (experienceLevelId != null) {
            job.setExperienceLevel(
                    experienceLevelRepository.findById(experienceLevelId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Experience level not found"))
            );
        }

        if (workplaceTypeId != null) {
            job.setWorkplaceType(
                    workplaceTypeRepository.findById(workplaceTypeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Workplace type not found"))
            );
        }

        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJob(Integer id) {
        return jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));
    }

    public Job updateJob(
            Integer id,
            String title,
            Integer companyId,
            String location,
            String salary,
            Integer cloudPlatformId,
            Integer employmentTypeId,
            Integer experienceLevelId,
            Integer workplaceTypeId,
            String description) {

        Job job = getJob(id);

        job.setTitle(title);
        job.setLocation(location);
        job.setSalary(salary);
        job.setDescription(description);

        if (companyId != null) {
            job.setCompany(
                    companyRepository.findById(companyId)
                            .orElseThrow(() ->
                                    new RuntimeException("Company not found"))
            );
        } else {
            job.setCompany(null);
        }

        if (cloudPlatformId != null) {
            job.setCloudPlatform(
                    cloudPlatformRepository.findById(cloudPlatformId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Cloud platform not found"))
            );
        } else {
            job.setCloudPlatform(null);
        }

        if (employmentTypeId != null) {
            job.setEmploymentType(
                    employmentTypeRepository.findById(employmentTypeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Employment type not found"))
            );
        } else {
            job.setEmploymentType(null);
        }

        if (experienceLevelId != null) {
            job.setExperienceLevel(
                    experienceLevelRepository.findById(experienceLevelId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Experience level not found"))
            );
        } else {
            job.setExperienceLevel(null);
        }

        if (workplaceTypeId != null) {
            job.setWorkplaceType(
                    workplaceTypeRepository.findById(workplaceTypeId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Workplace type not found"))
            );
        } else {
            job.setWorkplaceType(null);
        }

        return jobRepository.save(job);
    }

    public void deleteJob(Integer id) {

        if (!jobRepository.existsById(id)) {
            throw new RuntimeException("Job not found");
        }

        jobRepository.deleteById(id);
    }

    public List<Job> searchJobs(
            String title,
            String location) {

        if (title != null && !title.isBlank()
                && location != null && !location.isBlank()) {

            return jobRepository
                    .findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(
                            title,
                            location
                    );
        }

        if (title != null && !title.isBlank()) {
            return jobRepository
                    .findByTitleContainingIgnoreCase(title);
        }

        if (location != null && !location.isBlank()) {
            return jobRepository
                    .findByLocationContainingIgnoreCase(location);
        }

        return jobRepository.findAll();
    }
}