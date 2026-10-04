package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.CandidateSkillResponse;
import com.ascendingcloud.staffing.entity.CandidateProfile;
import com.ascendingcloud.staffing.entity.CandidateSkill;
import com.ascendingcloud.staffing.entity.Skill;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.CandidateProfileRepository;
import com.ascendingcloud.staffing.repository.CandidateSkillRepository;
import com.ascendingcloud.staffing.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CandidateSkillService {

    private final CandidateSkillRepository candidateSkillRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final SkillRepository skillRepository;

    public CandidateSkillService(
            CandidateSkillRepository candidateSkillRepository,
            CandidateProfileRepository candidateProfileRepository,
            SkillRepository skillRepository) {
        this.candidateSkillRepository = candidateSkillRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.skillRepository = skillRepository;
    }

    @Transactional(readOnly = true)
    public List<CandidateSkillResponse> getAllSkills() {
        return skillRepository.findAll()
                .stream()
                .map(skill -> new CandidateSkillResponse(skill.getId(), skill.getName()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CandidateSkillResponse> getCandidateSkills(User user) {
        CandidateProfile profile = profileOf(user);

        return candidateSkillRepository.findByCandidateId(profile.getId())
                .stream()
                .map(cs -> new CandidateSkillResponse(
                        cs.getSkillId(),
                        cs.getSkill().getName()))
                .toList();
    }

    @Transactional
    public CandidateSkillResponse addSkill(User user, Integer skillId) {
        CandidateProfile profile = profileOf(user);

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        if (candidateSkillRepository.existsByCandidateIdAndSkillId(profile.getId(), skillId)) {
            throw new BadRequestException("Skill already added");
        }

        CandidateSkill candidateSkill = new CandidateSkill();
        candidateSkill.setCandidateId(profile.getId());
        candidateSkill.setSkillId(skill.getId());
        candidateSkill.setCandidate(profile);
        candidateSkill.setSkill(skill);

        candidateSkillRepository.save(candidateSkill);

        return new CandidateSkillResponse(skill.getId(), skill.getName());
    }

    @Transactional
    public void removeSkill(User user, Integer skillId) {
        CandidateProfile profile = profileOf(user);

        if (!candidateSkillRepository.existsByCandidateIdAndSkillId(profile.getId(), skillId)) {
            throw new ResourceNotFoundException("Skill is not assigned to this candidate");
        }

        candidateSkillRepository.deleteByCandidateIdAndSkillId(profile.getId(), skillId);
    }

    private CandidateProfile profileOf(User user) {
        return candidateProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));
    }
}
