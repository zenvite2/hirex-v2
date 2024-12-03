package com.ptit.hirex.service;

import com.ptit.data.entity.*;
import com.ptit.data.repository.*;
import com.ptit.hirex.dto.request.ResumeRequest;
import com.ptit.hirex.dto.response.ResumeResponse;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public ResponseEntity<ResponseDto<Object>> createResume(ResumeRequest resumeRequest) {
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
            resume.setTitle(resumeRequest.getName());

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
                experienceMap.put("startDate", experience.getStartDate().toString());
                experienceMap.put("endDate", experience.getEndDate().toString());
                experienceMap.put("description", experience.getDescription());
                experienceList.add(experienceMap);
            }

            List<Education> educations = educationRepository.findAllByEmployeeId(employee.getId());
            List<Map<String, Object>> educationList = new ArrayList<>();

            for (Education education : educations) {
                Map<String, Object> educationMap = new HashMap<>();
                educationMap.put("id", education.getId());
                educationMap.put("name", education.getUniversityName());
                educationMap.put("startDate", education.getStartDate().toString());
                educationMap.put("endDate", education.getEndDate().toString());
                educationList.add(educationMap);
            }

            resume.setSkills(skillList);
            resume.setExperiences(experienceList);
            resume.setEducations(educationList);

            resumeRepository.save(resume);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.resume.success"),
                    resume,
                    StatusCodeEnum.RESUME1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.resume.failed"),
                    StatusCodeEnum.RESUME0000
            );
        }
    }

    public ResumeResponse findById(Long id) {
        Resume resume = resumeRepository.findById(id).orElse(null);
        Optional<Employee> employee = employeeRepository.findById(resume.getEmployeeId());
        User user = userRepository.findById(employee.get().getUserId()).orElse(null);
        ResumeResponse resumeResponse = modelMapper.map(resume, ResumeResponse.class);
        resumeResponse.setFullName(Objects.requireNonNull(user).getFullName());
        resumeResponse.setAvatar(user.getAvatar());
        resumeResponse.setAddress(employee.get().getAddress());
        resumeResponse.setEmail(user.getEmail());
        resumeResponse.setGender(employee.get().getGender());
        resumeResponse.setDateOfBirth(employee.get().getDateOfBirth());
        resumeResponse.setPhoneNumber(user.getPhoneNumber());
        return resumeResponse;
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
                    languageService.getMessage("get.resume.success"),
                    resumes,
                    StatusCodeEnum.RESUME1001
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.resume.failed"),
                    StatusCodeEnum.RESUME0001
            );
        }
    }

    @Transactional
    public ResponseEntity<ResponseDto<Object>> getAllResumeCMS() {
        try {
            List<Resume> resumes = resumeRepository.findAll();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.resume.success"),
                    resumes,
                    StatusCodeEnum.RESUME1001
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.resume.failed"),
                    StatusCodeEnum.RESUME0001
            );
        }
    }

    public Resume update(Long id, Resume resume) {
        if (!resumeRepository.existsById(id)) {
            throw new RuntimeException("Resume not found with id: " + resume.getId());
        }
        Optional<Resume> resumeDB = resumeRepository.findById(id);
        resume.setEmployeeId(resumeDB.get().getEmployeeId());
        resume.setTitle(resumeDB.get().getTitle());

        return resumeRepository.save(resume);
    }

    public ResponseEntity<ResponseDto<Object>> deleteResume(Long id) {
        try {

            resumeRepository.deleteById(id);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("delete.resume.success"),
                    StatusCodeEnum.RESUME1002
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("delete.resume.failed"),
                    StatusCodeEnum.RESUME0002
            );
        }
    }
}