package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.CandidateSkillResponse;
import com.ascendingcloud.staffing.entity.CandidateProfile;
import com.ascendingcloud.staffing.entity.CandidateSkill;
import com.ascendingcloud.staffing.entity.Skill;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.repository.CandidateProfileRepository;
import com.ascendingcloud.staffing.repository.CandidateSkillRepository;
import com.ascendingcloud.staffing.repository.SkillRepository;
import com.ascendingcloud.staffing.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CandidateSkillService {

    private final CandidateSkillRepository candidateSkillRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public CandidateSkillService(
            CandidateSkillRepository candidateSkillRepository,
            CandidateProfileRepository candidateProfileRepository,
            SkillRepository skillRepository,
            UserRepository userRepository
    ) {
        this.candidateSkillRepository = candidateSkillRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    public List<CandidateSkillResponse> getAllSkills() {
        return skillRepository.findAll()
                .stream()
                .map(skill -> new CandidateSkillResponse(
                        skill.getId(),
                        skill.getName()
                ))
                .toList();
    }

    public List<CandidateSkillResponse> getCandidateSkills(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"CANDIDATE".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Only candidates can access candidate skills");
        }

        CandidateProfile candidateProfile =
                candidateProfileRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Candidate profile not found"));

        return candidateSkillRepository
                .findByCandidateId(candidateProfile.getId())
                .stream()
                .map(candidateSkill -> new CandidateSkillResponse(
                        candidateSkill.getSkillId(),
                        candidateSkill.getSkill().getName()
                ))
                .toList();
    }

    public CandidateSkillResponse addSkill(
            String email,
            Integer skillId
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"CANDIDATE".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Only candidates can add skills");
        }

        CandidateProfile candidateProfile =
                candidateProfileRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Candidate profile not found"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() ->
                        new RuntimeException("Skill not found"));

        if (candidateSkillRepository.existsByCandidateIdAndSkillId(
                candidateProfile.getId(),
                skillId
        )) {
            throw new RuntimeException("Skill already added");
        }

        CandidateSkill candidateSkill = new CandidateSkill();

        candidateSkill.setCandidateId(candidateProfile.getId());
        candidateSkill.setSkillId(skill.getId());
        candidateSkill.setCandidate(candidateProfile);
        candidateSkill.setSkill(skill);

        candidateSkillRepository.save(candidateSkill);

        return new CandidateSkillResponse(
                skill.getId(),
                skill.getName()
        );
    }

    @Transactional
    public void removeSkill(
            String email,
            Integer skillId
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"CANDIDATE".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Only candidates can remove skills");
        }

        CandidateProfile candidateProfile =
                candidateProfileRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Candidate profile not found"));

        if (!candidateSkillRepository.existsByCandidateIdAndSkillId(
                candidateProfile.getId(),
                skillId
        )) {
            throw new RuntimeException("Skill is not assigned to this candidate");
        }

        candidateSkillRepository.deleteByCandidateIdAndSkillId(
                candidateProfile.getId(),
                skillId
        );
    }
}