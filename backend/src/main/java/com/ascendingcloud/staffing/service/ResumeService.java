package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.ResumeDTO;
import com.ascendingcloud.staffing.entity.CandidateProfile;
import com.ascendingcloud.staffing.entity.Resume;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.mapper.ResumeMapper;
import com.ascendingcloud.staffing.repository.ApplicationRepository;
import com.ascendingcloud.staffing.repository.CandidateProfileRepository;
import com.ascendingcloud.staffing.repository.ResumeRepository;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.storage.FileValidator;
import com.ascendingcloud.staffing.storage.StorageService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * One active resume per candidate (uploading again replaces it).
 * Resumes are private: the candidate, admins, and employers who received an
 * application from that candidate are the only ones who can download them.
 */
@Service
public class ResumeService {

    public record ResumeFile(InputStream stream, String contentType, String fileName) {
    }

    private final ResumeRepository resumeRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final ApplicationRepository applicationRepository;
    private final CurrentUserService currentUserService;
    private final StorageService storageService;

    public ResumeService(
            ResumeRepository resumeRepository,
            CandidateProfileRepository candidateProfileRepository,
            ApplicationRepository applicationRepository,
            CurrentUserService currentUserService,
            StorageService storageService) {
        this.resumeRepository = resumeRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.applicationRepository = applicationRepository;
        this.currentUserService = currentUserService;
        this.storageService = storageService;
    }

    @Transactional
    public ResumeDTO upload(User user, MultipartFile file) {

        CandidateProfile profile = candidateProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));

        String ext = FileValidator.validateResume(file);
        String key = FileValidator.secureKey("resumes", ext);

        try (InputStream in = file.getInputStream()) {
            storageService.store(key, in, file.getSize());
        } catch (IOException e) {
            throw new IllegalStateException("Could not store resume", e);
        }

        Resume resume = resumeRepository.findByCandidateId(profile.getId()).orElse(null);
        String oldKey = null;

        if (resume == null) {
            resume = new Resume();
            resume.setCandidate(profile);
        } else {
            oldKey = resume.getStorageKey();
        }

        String original = file.getOriginalFilename();

        resume.setOriginalFileName(
                original == null ? "resume." + ext : sanitise(original));
        resume.setStorageKey(key);
        resume.setContentType(FileValidator.resumeContentType(ext));
        resume.setFileSize(file.getSize());
        resume.setUploadedAt(LocalDateTime.now());

        Resume saved = resumeRepository.save(resume);

        if (oldKey != null) {
            storageService.delete(oldKey);
        }

        return ResumeMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public ResumeDTO getMyResume(User user) {
        return ResumeMapper.toDTO(findMine(user));
    }

    @Transactional(readOnly = true)
    public ResumeFile download(User user, Integer resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        assertCanView(user, resume);

        return new ResumeFile(
                storageService.load(resume.getStorageKey()),
                resume.getContentType(),
                resume.getOriginalFileName());
    }

    /** Resume metadata for a candidate, visible to authorised viewers only. */
    @Transactional(readOnly = true)
    public ResumeDTO getForCandidate(User viewer, Integer candidateId) {
        Resume resume = resumeRepository.findByCandidateId(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        assertCanView(viewer, resume);

        return ResumeMapper.toDTO(resume);
    }

    @Transactional
    public void deleteMyResume(User user) {
        Resume resume = findMine(user);

        storageService.delete(resume.getStorageKey());
        resumeRepository.delete(resume);
    }

    private Resume findMine(User user) {
        CandidateProfile profile = candidateProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));

        return resumeRepository.findByCandidateId(profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("You have not uploaded a resume"));
    }

    private void assertCanView(User viewer, Resume resume) {

        if (currentUserService.isAdmin(viewer)) {
            return;
        }

        CandidateProfile owner = resume.getCandidate();

        if (owner.getUser().getId().equals(viewer.getId())) {
            return;
        }

        if (currentUserService.isEmployer(viewer)) {
            Integer companyId = currentUserService.getEmployerCompanyId(viewer);

            if (companyId != null
                    && applicationRepository.existsByCandidateIdAndJobCompanyId(
                            owner.getId(), companyId)) {
                return;
            }
        }

        throw new AccessDeniedException("You are not allowed to access this resume");
    }

    /** Keeps only a safe display name (never used as a path on disk). */
    private String sanitise(String name) {
        String base = name.replace("\\", "/");
        base = base.substring(base.lastIndexOf('/') + 1);
        base = base.replaceAll("[^A-Za-z0-9._ -]", "_");

        return base.length() > 200 ? base.substring(base.length() - 200) : base;
    }
}
