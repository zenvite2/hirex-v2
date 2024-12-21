package com.ptit.hirex.service;

import com.ptit.data.entity.CareerGoal;
import com.ptit.data.entity.Employee;
import com.ptit.data.repository.CareerGoalRepository;
import com.ptit.data.repository.EmployeeRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.request.CareerGoalRequest;
import com.ptit.hirex.dto.response.CareerGoalResponse;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CareerGoalService {
    private final CareerGoalRepository careerGoalRepository;
    private final ModelMapper modelMapper;
    private final LanguageService languageService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public ResponseEntity<ResponseDto<Object>> createCareerGoal(CareerGoalRequest careerGoalRequest) {
        Employee employee = authenticationService.getEmployeeFromContext();

        if (employee == null) {
            log.error("EmployeeId is null");
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            CareerGoal careerGoal = CareerGoal.builder()
                    .industryId(careerGoalRequest.getIndustry())
                    .jobTypeId(careerGoalRequest.getJobType())
                    .positionId(careerGoalRequest.getPosition())
                    .maxSalary(careerGoalRequest.getMaxSalary())
                    .employeeId(employee.getId())
                    .build();

            careerGoalRepository.save(careerGoal);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.career.success"),
                    careerGoal,
                    StatusCodeEnum.CAREER1000
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.career.failed"),
                    StatusCodeEnum.CAREER0000
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> updateCareerGoal(Long id, CareerGoalRequest careerGoalRequest) {
        try {
            CareerGoal careerGoal = careerGoalRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Career goal not found"));

            if (careerGoalRequest != null) {
                modelMapper.map(careerGoalRequest, careerGoal);
                careerGoal.setIndustryId(careerGoalRequest.getIndustry());
                careerGoal.setIndustryId(careerGoalRequest.getIndustry());
                careerGoal.setPositionId(careerGoalRequest.getPosition());
            }

            careerGoalRepository.save(careerGoal);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.career.success"),
                    careerGoal,
                    StatusCodeEnum.CAREER1000
            );
        } catch (NoSuchElementException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("career.not.found"),
                    StatusCodeEnum.CAREER0001
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("update.career.failed"),
                    StatusCodeEnum.CAREER0000
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getCareerGoal() {

        Employee employee = authenticationService.getEmployeeFromContext();

        if (employee == null) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            CareerGoal careerGoal = careerGoalRepository.findByEmployeeId(employee.getId());

            if (careerGoal == null) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("get.career.failed"),
                        StatusCodeEnum.CAREER0002
                );
            }

            CareerGoalResponse careerGoalResponse = CareerGoalResponse.builder()
                    .industry(careerGoal.getIndustryId())
                    .position(careerGoal.getPositionId())
                    .maxSalary(careerGoal.getMaxSalary())
                    .minSalary(careerGoal.getMinSalary())
                    .jobType(careerGoal.getJobTypeId())
                    .id(careerGoal.getId())
                    .build();


            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.career.success"),
                    careerGoalResponse,
                    StatusCodeEnum.CAREER1002
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.career.failed"),
                    StatusCodeEnum.CAREER0002
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> deleteCareerGoal(Long id) {
        try {
            careerGoalRepository.deleteById(id);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("delete.career.success"),
                    StatusCodeEnum.CAREER1002
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("delete.career.failed"),
                    StatusCodeEnum.CAREER0002
            );
        }
    }
}
