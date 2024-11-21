package com.ptit.hirex.service;

import com.ptit.data.entity.*;
import com.ptit.data.repository.*;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final LanguageService languageService;
    private final AuthenticationService authenticationService;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final SkillRepository skillRepository;
    private final ExperienceRepository experienceRepository;
    private final PositionRepository positionRepository;
    private final EducationRepository educationRepository;

    public ResponseEntity<ResponseDto<Object>> createResume() {
        try {
            Employee employee = authenticationService.getEmployeeFromContext();

            if (employee == null) {
                return ResponseBuilder.okResponse(
                        languageService.getMessage("employee.not.found"),
                        StatusCodeEnum.EMPLOYEE4000
                );
            }

            Resume resume = new Resume();
            resume.setEmployeeId(employee.getId());

            List<EmployeeSkill> employeeSkills = employeeSkillRepository.findAllByEmployeeId(employee.getId());

            List<Map<String, Object>> skillList = new ArrayList<>();

            for (EmployeeSkill employeeSkill : employeeSkills) {
                Skill skill = skillRepository.findById(employeeSkill.getSkillId()).orElse(null);
                if (skill != null) {
                    Map<String, Object> skillMap = new HashMap<>();
                    skillMap.put("id", skill.getId());
                    skillMap.put("name", skill.getName());
                    skillList.add(skillMap);
                }
            }

            List<Experience> experiences = experienceRepository.findAllByEmployeeId(employee.getId());

            List<Map<String, Object>> experienceList = new ArrayList<>();

            for (Experience experience : experiences) {
                Map<String, Object> experienceMap = new HashMap<>();
                experienceMap.put("id", experience.getId());
                experienceMap.put("company", experience.getCompanyName());
                experienceMap.put("position", positionRepository.findById(experience.getPosition()).get().getName());
                experienceMap.put("startDate", experience.getStartDate());
                experienceMap.put("endDate", experience.getEndDate());
                experienceMap.put("description", experience.getDescription());
                experienceList.add(experienceMap);
            }

            List<Education> educations = educationRepository.findAllByEmployeeId(employee.getId());
            List<Map<String, Object>> educationList = new ArrayList<>();

            for (Education education : educations) {
                Map<String, Object> educationMap = new HashMap<>();
                educationMap.put("id", education.getId());
                educationMap.put("name", education.getUniversityName());
                educationMap.put("startDate", education.getStartDate());
                educationMap.put("endDate", education.getEndDate());
                educationList.add(educationMap);
            }

            resume.setSkills(skillList);
            resume.setExperiences(experienceList);
            resume.setEducations(educationList);

            resumeRepository.save(resume);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("mark.notifications.read.success"),
                    StatusCodeEnum.NOTIFICATION1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("mark.notifications.read.failed"),
                    StatusCodeEnum.NOTIFICATION0000
            );
        }
    }

    public Resume findById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found with id: " + id));
    }

    public List<Resume> findAll() {
        return resumeRepository.findAll();
    }

    @Transactional
    public ResponseEntity<ResponseDto<Object>> getAllResume() {
        try {
            Employee employee = authenticationService.getEmployeeFromContext();

            if (employee == null) {
                return ResponseBuilder.okResponse(
                        languageService.getMessage("employee.not.found"),
                        StatusCodeEnum.EMPLOYEE4000
                );
            }


            List<Resume> resumes = resumeRepository.findByEmployeeId(employee.getId());

            return ResponseBuilder.okResponse(
                    languageService.getMessage("mark.notifications.read.success"),
                    resumes,
                    StatusCodeEnum.NOTIFICATION1000
            );
        }catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("mark.notifications.read.failed"),
                    StatusCodeEnum.NOTIFICATION0000
            );
        }
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