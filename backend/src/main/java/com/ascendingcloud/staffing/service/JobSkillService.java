package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Job;
import com.ascendingcloud.staffing.entity.JobSkill;
import com.ascendingcloud.staffing.entity.Skill;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.JobRepository;
import com.ascendingcloud.staffing.repository.JobSkillRepository;
import com.ascendingcloud.staffing.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobSkillService {

    private final JobSkillRepository jobSkillRepository;
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final JobService jobService;

    public JobSkillService(
            JobSkillRepository jobSkillRepository,
            JobRepository jobRepository,
            SkillRepository skillRepository,
            JobService jobService) {
        this.jobSkillRepository = jobSkillRepository;
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.jobService = jobService;
    }

    @Transactional(readOnly = true)
    public List<Skill> getJobSkills(Integer jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job not found");
        }

        // Copy into plain objects so no lazy Hibernate proxy reaches the JSON layer
        return jobSkillRepository.findByJobId(jobId)
                .stream()
                .map(JobSkill::getSkill)
                .map(s -> {
                    Skill copy = new Skill();
                    copy.setId(s.getId());
                    copy.setName(s.getName());
                    return copy;
                })
                .toList();
    }

    @Transactional
    public Skill addSkill(User user, Integer jobId, Integer skillId) {
        Job job = jobService.find(jobId);
        jobService.assertCanManage(user, job);

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        if (jobSkillRepository.existsByJobIdAndSkillId(jobId, skillId)) {
            throw new BadRequestException("Skill already assigned to this job");
        }

        JobSkill jobSkill = new JobSkill();
        jobSkill.setJobId(job.getId());
        jobSkill.setSkillId(skill.getId());
        jobSkill.setJob(job);
        jobSkill.setSkill(skill);

        jobSkillRepository.save(jobSkill);

        return skill;
    }

    @Transactional
    public void removeSkill(User user, Integer jobId, Integer skillId) {
        Job job = jobService.find(jobId);
        jobService.assertCanManage(user, job);

        if (!jobSkillRepository.existsByJobIdAndSkillId(jobId, skillId)) {
            throw new ResourceNotFoundException("Skill is not assigned to this job");
        }

        jobSkillRepository.deleteByJobIdAndSkillId(jobId, skillId);
    }
}
