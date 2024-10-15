package com.ptit.hirex.service;

import com.ptit.data.entity.Experience;
import com.ptit.data.entity.Skill;
import com.ptit.data.repository.SkillRepository;
import com.ptit.hirex.dto.request.ExperienceRequest;
import com.ptit.hirex.dto.request.SkillRequest;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private final ModelMapper modelMapper;
    private final LanguageService languageService;
    private final AuthenticationService authenticationService;

    public ResponseEntity<ResponseDto<Object>> createSkill(SkillRequest skillRequest) {
        Long employeeId = authenticationService.getEmployeeFromContext();

        if(employeeId == null){
            log.error("EmployeeId is null");
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            Skill skill = modelMapper.map(skillRequest, Skill.class);
            skill.setEmployeeId(employeeId);

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
            Skill skill = modelMapper.map(skillRequest, Skill.class);

            skillRepository.save(skill);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.skill.success"),
                    skill,
                    StatusCodeEnum.SKILL1001
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

    public ResponseEntity<ResponseDto<List<Skill>>> getAllSkill() {

        Long employeeId = authenticationService.getEmployeeFromContext();

        if(employeeId == null){
            log.error("EmployeeId is null");
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            List<Skill> skill = skillRepository.findAllByEmployeeId(employeeId);

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
