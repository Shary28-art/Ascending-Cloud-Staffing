package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Resume;
import com.ascendingcloud.staffing.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }

    public Resume getResumeById(Integer id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
    }

    public Resume createResume(Resume resume) {
        if (resume.getUploadedAt() == null) {
            resume.setUploadedAt(LocalDateTime.now());
        }

        return resumeRepository.save(resume);
    }

    public Resume updateResume(Integer id, Resume resume) {

        Resume existing = getResumeById(id);

        existing.setCandidate(resume.getCandidate());
        existing.setFileName(resume.getFileName());
        existing.setFilePath(resume.getFilePath());

        return resumeRepository.save(existing);
    }

    public void deleteResume(Integer id) {

        if (!resumeRepository.existsById(id)) {
            throw new RuntimeException("Resume not found");
        }

        resumeRepository.deleteById(id);
    }
}