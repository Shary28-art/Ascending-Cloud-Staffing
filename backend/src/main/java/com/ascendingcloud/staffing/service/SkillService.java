package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Skill;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository repository;

    public SkillService(SkillRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Skill> getAllSkills() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Skill getSkillById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Skill not found with id: " + id));
    }

    @Transactional
    public Skill createSkill(Skill entity) {
        String name = cleanName(entity.getName());

        Skill created = new Skill();
        created.setName(name);

        return repository.save(created);
    }

    @Transactional
    public Skill updateSkill(Integer id, Skill entity) {
        Skill existing = getSkillById(id);

        existing.setName(cleanName(entity.getName()));

        return repository.save(existing);
    }

    @Transactional
    public void deleteSkill(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Skill not found with id: " + id);
        }

        repository.deleteById(id);
    }

    private String cleanName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Skill name is required");
        }

        return name.trim();
    }
}
