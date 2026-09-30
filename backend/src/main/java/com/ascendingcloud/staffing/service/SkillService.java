package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Skill;
import com.ascendingcloud.staffing.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Skill getSkillById(Integer id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
    }

    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public Skill updateSkill(Integer id, Skill skill) {
        Skill existingSkill = getSkillById(id);

        existingSkill.setName(skill.getName());

        return skillRepository.save(existingSkill);
    }

    public void deleteSkill(Integer id) {
        if (!skillRepository.existsById(id)) {
            throw new RuntimeException("Skill not found");
        }

        skillRepository.deleteById(id);
    }
}