package com.ptit.hirex.service;

import com.ptit.data.entity.Education;
import com.ptit.data.repository.EducationRepository;
import com.ptit.hirex.dto.request.EducationRequest;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EducationService {

    private final ModelMapper modelMapper;
    private final EducationRepository educationRepository;
    private final LanguageService languageService;
    private final AuthenticationService authenticationService;

    public ResponseEntity<ResponseDto<Object>> createEducation(EducationRequest educationRequest) {
        Long employeeId = authenticationService.getEmployeeFromContext();

        if (employeeId == null) {
            log.error("EmployeeId is null");
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            Education education = modelMapper.map(educationRequest, Education.class);
            education.setEmployeeId(employeeId);

            educationRepository.save(education);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.education.success"),
                    education,
                    StatusCodeEnum.EDUCATION1000
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.education.failed"),
                    StatusCodeEnum.EDUCATION0000
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> updateEducation(Long id, EducationRequest educationRequest) {

        try {
            Education education = modelMapper.map(educationRequest, Education.class);

            educationRepository.save(education);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.education.success"),
                    education,
                    StatusCodeEnum.EDUCATION1001
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("update.education.failed"),
                    StatusCodeEnum.EDUCATION0001
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getEducation(Long id) {
        try {
            Education education = educationRepository.findById(id).orElse(null);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.education.success"),
                    education,
                    StatusCodeEnum.EDUCATION1002
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.education.failed"),
                    StatusCodeEnum.EDUCATION0002
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> deleteEducation(Long id) {
        try {
            educationRepository.deleteById(id);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("delete.education.success"),
                    StatusCodeEnum.EDUCATION1003
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("delete.education.failed"),
                    StatusCodeEnum.EDUCATION0003
            );
        }
    }
}