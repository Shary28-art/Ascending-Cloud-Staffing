package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.SavedJobDTO;
import com.ascendingcloud.staffing.entity.Job;
import com.ascendingcloud.staffing.entity.SavedJob;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.mapper.JobMapper;
import com.ascendingcloud.staffing.repository.SavedJobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobService jobService;

    public SavedJobService(
            SavedJobRepository savedJobRepository,
            JobService jobService) {
        this.savedJobRepository = savedJobRepository;
        this.jobService = jobService;
    }

    @Transactional(readOnly = true)
    public List<SavedJobDTO> getMySavedJobs(User user) {
        return savedJobRepository.findByUserIdOrderBySavedAtDesc(user.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public SavedJobDTO saveJob(User user, Integer jobId) {
        Job job = jobService.find(jobId);

        if (!jobService.isPublic(job)) {
            throw new BadRequestException("This job is not available");
        }

        if (savedJobRepository.existsByUserIdAndJobId(user.getId(), jobId)) {
            throw new BadRequestException("Job is already saved");
        }

        SavedJob savedJob = new SavedJob();
        savedJob.setUser(user);
        savedJob.setJob(job);

        return toDTO(savedJobRepository.save(savedJob));
    }

    @Transactional
    public void unsaveJob(User user, Integer jobId) {
        SavedJob savedJob = savedJobRepository.findByUserIdAndJobId(user.getId(), jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Saved job not found"));

        savedJobRepository.delete(savedJob);
    }

    private SavedJobDTO toDTO(SavedJob s) {
        return new SavedJobDTO(
                s.getId(),
                s.getJob().getId(),
                s.getSavedAt(),
                JobMapper.toDTO(s.getJob()));
    }
}
