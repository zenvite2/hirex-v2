package com.ptit.hirex.service;

import com.ptit.data.entity.Employee;
import com.ptit.data.entity.Skill;
import com.ptit.data.entity.Tech;
import com.ptit.data.repository.SkillRepository;
import com.ptit.data.repository.TechRepository;
import com.ptit.hirex.dto.request.SkillRequest;
import com.ptit.hirex.dto.response.SkillResponse;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private final ModelMapper modelMapper;
    private final LanguageService languageService;
    private final AuthenticationService authenticationService;
    private final TechRepository techRepository;

    public ResponseEntity<ResponseDto<Object>> createSkill(SkillRequest skillRequest) {
        Employee employee = authenticationService.getEmployeeFromContext();

        if (employee == null) {
            log.error("EmployeeId is null");
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            Skill skill = modelMapper.map(skillRequest, Skill.class);

            skillRepository.save(skill);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.skill.success"),
                    skill,
                    StatusCodeEnum.SKILL1000
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.skill.failed"),
                    StatusCodeEnum.SKILL0000
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> updateSkill(Long id, SkillRequest skillRequest) {
        try {
            Skill skill = skillRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(languageService.getMessage("not.found.skill")));

            if (skillRequest != null) {
                modelMapper.map(skillRequest, skill);
            }

            skillRepository.save(skill);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.skill.success"),
                    skill,
                    StatusCodeEnum.SKILL1001
            );
        } catch (NoSuchElementException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("not.found.skill"),
                    StatusCodeEnum.SKILL4000
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("update.skill.failed"),
                    StatusCodeEnum.SKILL0001
            );
        }
    }


    public ResponseEntity<ResponseDto<Object>> getSkill(Long id) {

        try {
            Skill skill = skillRepository.findById(id).orElse(null);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.skill.success"),
                    skill,
                    StatusCodeEnum.SKILL1002
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.skill.failed"),
                    StatusCodeEnum.SKILL0002
            );
        }
    }

//    public ResponseEntity<ResponseDto<List<SkillResponse>>> getAllSkill() {
//        Employee employee = authenticationService.getEmployeeFromContext();
//
//        if (employee == null) {
//            log.error("EmployeeId is null");
//            return ResponseBuilder.badRequestResponse(
//                    languageService.getMessage("employee.not.found"),
//                    StatusCodeEnum.AUTH0016
//            );
//        }
//
//        try {
//            List<Skill> skills = skillRepository.findAllByEmployeeId(employee.getId());
//            List<SkillResponse> skillResponses = skills.stream()
//                    .map(skill -> {
//                        // Lấy technology name từ repository
//                        String techName = null;
//                        if (skill.getTechId() != null) {
//                            Tech tech = techRepository.findById(skill.getTechId())
//                                    .orElse(null);
//                            if (tech != null) {
//                                techName = tech.getName();
//                            }
//                        }
//
//                        return SkillResponse.builder()
//                                .skillName(null)
//                                .techId(skill.getTechId())
//                                .techName(techName)
//                                .level(skill.getLevel())
//                                .description(skill.getDescription())
//                                .build();
//                    })
//                    .collect(Collectors.toList());
//
//            return ResponseBuilder.okResponse(
//                    languageService.getMessage("get.skill.success"),
//                    skillResponses,
//                    StatusCodeEnum.SKILL1002
//            );
//        } catch (RuntimeException e) {
//            log.error("Error getting skills for employee {}: {}", employee.getId(), e.getMessage());
//            return ResponseBuilder.badRequestResponse(
//                    languageService.getMessage("get.skill.failed"),
//                    StatusCodeEnum.SKILL0002
//            );
//        }
//    }

    public ResponseEntity<ResponseDto<Object>> deleteSkill(Long id) {

        try {
            skillRepository.deleteById(id);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("delete.skill.success"),
                    StatusCodeEnum.SKILL1003
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("delete.skill.failed"),
                    StatusCodeEnum.SKILL0003
            );
        }
    }
}
