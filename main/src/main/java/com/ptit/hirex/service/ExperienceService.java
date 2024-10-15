package com.ptit.hirex.service;

import com.ptit.data.entity.Experience;
import com.ptit.data.repository.ExperienceRepository;
import com.ptit.hirex.dto.request.ExperienceRequest;
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
public class ExperienceService {

    private final ModelMapper modelMapper;
    private final ExperienceRepository experienceRepository;
    private final LanguageService languageService;
    private final AuthenticationService authenticationService;

    public ResponseEntity<ResponseDto<Object>> createExperience(ExperienceRequest experienceRequest) {
        Long employeeId = authenticationService.getEmployeeFromContext();

        if(employeeId == null){
            log.error("EmployeeId is null");
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            Experience experience = modelMapper.map(experienceRequest, Experience.class);
            experience.setEmployeeId(employeeId);

            experienceRepository.save(experience);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.experience.success"),
                    experience,
                    StatusCodeEnum.EXPERIENCE1000
            );
        } catch (
                RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.experience.failed"),
                    StatusCodeEnum.EXPERIENCE0000
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> updateExperience(Long id, ExperienceRequest experienceRequest) {

        try {
            Experience experience = modelMapper.map(experienceRequest, Experience.class);

            experienceRepository.save(experience);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.experience.success"),
                    experience,
                    StatusCodeEnum.EXPERIENCE1001
            );
        } catch (
                RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("update.experience.failed"),
                    StatusCodeEnum.EXPERIENCE0001
            );
        }
    }

    public ResponseEntity<ResponseDto<List<Experience>>> getExperience() {

        Long employeeId = authenticationService.getEmployeeFromContext();

        if(employeeId == null){
            log.error("EmployeeId is null");
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            List<Experience> experience = experienceRepository.findAllByEmployeeId(employeeId);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.experience.success"),
                    experience,
                    StatusCodeEnum.EXPERIENCE1002
            );
        }catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.experience.failed"),
                    StatusCodeEnum.EXPERIENCE1002
            );
        }
    }

    public ResponseEntity<ResponseDto<Experience>> getExperienceById(Long id) {

        try {
            Experience experience = experienceRepository.findById(id).orElseThrow();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.experience.success"),
                    experience,
                    StatusCodeEnum.EXPERIENCE1002
            );
        }catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.experience.failed"),
                    StatusCodeEnum.EXPERIENCE1002
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> deleteExperience(Long id) {
        try {
            experienceRepository.deleteById(id);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("delete.experience.success"),
                    StatusCodeEnum.EXPERIENCE1003
            );
        }catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("delete.experience.failed"),
                    StatusCodeEnum.EXPERIENCE1003
            );
        }
    }
}