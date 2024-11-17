package com.ptit.hirex.service;

import com.ptit.data.entity.Education;
import com.ptit.data.entity.Employee;
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

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EducationService {

    private final ModelMapper modelMapper;
    private final EducationRepository educationRepository;
    private final LanguageService languageService;
    private final AuthenticationService authenticationService;

    public ResponseEntity<ResponseDto<Object>> createEducation(EducationRequest educationRequest) {
        Employee employee = authenticationService.getEmployeeFromContext();

        if (employee == null) {
            log.error("EmployeeId is null");
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            Education education = modelMapper.map(educationRequest, Education.class);
            education.setEmployeeId(employee.getId());

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
            Education education = educationRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(languageService.getMessage("not.found.education")));

            if (educationRequest != null) {
                modelMapper.map(educationRequest, education);
            }

            educationRepository.save(education);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.education.success"),
                    education,
                    StatusCodeEnum.EDUCATION1001
            );
        } catch (NoSuchElementException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("not.found.education"),
                    StatusCodeEnum.EDUCATION4000
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

    public ResponseEntity<ResponseDto<List<Education>>> getAllEducation() {

        Employee employee = authenticationService.getEmployeeFromContext();

        if (employee == null) {
            log.error("EmployeeId is null");
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            List<Education> education = educationRepository.findAllByEmployeeId(employee.getId());
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