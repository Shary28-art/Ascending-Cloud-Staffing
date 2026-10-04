package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.SavedJob;
import com.ascendingcloud.staffing.repository.SavedJobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;

    public SavedJobService(SavedJobRepository savedJobRepository) {
        this.savedJobRepository = savedJobRepository;
    }

    public List<SavedJob> getAllSavedJobs() {
        return savedJobRepository.findAll();
    }

    public SavedJob getSavedJobById(Integer id) {
        return savedJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Saved job not found"));
    }

    public SavedJob createSavedJob(SavedJob savedJob) {

        if (savedJob.getSavedAt() == null) {
            savedJob.setSavedAt(LocalDateTime.now());
        }

        return savedJobRepository.save(savedJob);
    }

    public void deleteSavedJob(Integer id) {

        if (!savedJobRepository.existsById(id)) {
            throw new RuntimeException("Saved job not found");
        }

        savedJobRepository.deleteById(id);
    }
}