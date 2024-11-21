package com.ptit.hirex.service;

import com.ptit.data.entity.Resume;
import com.ptit.data.repository.EducationLevelRepository;
import com.ptit.data.repository.EducationRepository;
import com.ptit.data.repository.EmployeeSkillRepository;
import com.ptit.data.repository.ExperienceRepository;
import com.ptit.data.repository.PositionRepository;
import com.ptit.data.repository.ResumeRepository;
import com.ptit.data.repository.SkillRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final EducationRepository educationRepository;
    private final EducationLevelRepository educationLevelRepository;
    private final ExperienceRepository experienceRepository;
    private final PositionRepository positionRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final SkillRepository skillRepository;

    public Long createNewResume(Long employeeId) {
        Resume resume = Resume.builder()
                .educations(educationRepository.findAllByEmployeeId(employeeId).stream()
                        .map(education -> {
                            education.setEducationLevel(
                                    educationLevelRepository.findById(education.getEducationLevelId()).orElse(null));
                            return education;
                        }).toList())
                .experiences(experienceRepository.findAllByEmployeeId(employeeId).stream()
                        .map(ex -> {
                            ex.setPositionObj(positionRepository.findById(ex.getPosition()).orElse(null));
                            return ex;
                        }).toList())
                .skills(employeeSkillRepository.findByEmployeeId(employeeId).stream()
                        .map(employeeSkill -> {
                            return skillRepository.findById(employeeSkill.getSkillId()).orElse(null);
                        }).toList())
                .build();
        Resume newResume = resumeRepository.save(resume);
        return newResume.getId();
    }

    public Resume save(Resume resume) {
        return resumeRepository.save(resume);
    }

    public Resume findById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found with id: " + id));
    }

    public List<Resume> findAll() {
        return resumeRepository.findAll();
    }

    public Resume update(Resume resume) {
        if (!resumeRepository.existsById(resume.getId())) {
            throw new RuntimeException("Resume not found with id: " + resume.getId());
        }
        return resumeRepository.save(resume);
    }

    public void deleteById(Long id) {
        resumeRepository.deleteById(id);
    }
}