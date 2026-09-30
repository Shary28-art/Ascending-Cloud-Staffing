package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Job;
import com.ascendingcloud.staffing.entity.JobSkill;
import com.ascendingcloud.staffing.entity.Skill;
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

    public JobSkillService(
            JobSkillRepository jobSkillRepository,
            JobRepository jobRepository,
            SkillRepository skillRepository) {

        this.jobSkillRepository = jobSkillRepository;
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public List<Skill> getJobSkills(Integer jobId) {

        if (!jobRepository.existsById(jobId)) {
            throw new RuntimeException("Job not found");
        }

        return jobSkillRepository.findByJobId(jobId)
                .stream()
                .map(JobSkill::getSkill)
                .toList();
    }

    public Skill addSkill(
            Integer jobId,
            Integer skillId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() ->
                        new RuntimeException("Skill not found"));

        if (jobSkillRepository.existsByJobIdAndSkillId(
                jobId,
                skillId)) {

            throw new RuntimeException(
                    "Skill already assigned to this job");
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
    public void removeSkill(
            Integer jobId,
            Integer skillId) {

        if (!jobRepository.existsById(jobId)) {
            throw new RuntimeException("Job not found");
        }

        if (!jobSkillRepository.existsByJobIdAndSkillId(
                jobId,
                skillId)) {

            throw new RuntimeException(
                    "Skill is not assigned to this job");
        }

        jobSkillRepository.deleteByJobIdAndSkillId(
                jobId,
                skillId
        );
    }
}