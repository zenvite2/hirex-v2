package com.ptit.hirex.service;

import com.ptit.data.entity.*;
import com.ptit.data.repository.*;
import com.ptit.hirex.dto.request.FollowCompanyRequest;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowCompanyService {

    private final LanguageService languageService;
    private final AuthenticationService authenticationService;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final FollowCompanyRepository followCompanyRepository;

    @Transactional
    public ResponseEntity<ResponseDto<Object>> followCompany(FollowCompanyRequest followCompanyRequest) {
        Employee employee = employeeRepository.findByUserId(followCompanyRequest.getEmployeeId());

        if (employee == null) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            Optional<Company> optionalCompany = companyRepository.findById(followCompanyRequest.getCompanyId());
            if (optionalCompany.isEmpty()) {
                return ResponseBuilder.okResponse(
                        languageService.getMessage("company.not.found"),
                        StatusCodeEnum.COMPANY0002
                );
            }

            FollowCompany followCompany = FollowCompany.builder()
                    .companyId(followCompanyRequest.getCompanyId())
                    .employeeId(followCompanyRequest.getEmployeeId())
                    .build();

            followCompanyRepository.save(followCompany);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.follow.company.success"),
                    followCompany,
                    StatusCodeEnum.FOLLOW1000
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.follow.company.failed"),
                    StatusCodeEnum.FOLLOW0000
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getFollowCompany() {
        Employee employee = authenticationService.getEmployeeFromContext();

        if (employee == null) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            List<FollowCompany> followCompanyList = followCompanyRepository.findAllByEmployeeId(employee.getUserId());

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.follow.company.success"),
                    followCompanyList,
                    StatusCodeEnum.FOLLOW1002
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.follow.company.failed"),
                    StatusCodeEnum.FOLLOW0002
            );
        }
    }

    public List<FollowCompany> getListFollow() {
        try {
            Employee employee = authenticationService.getEmployeeFromContext();

            return followCompanyRepository.findAllByEmployeeId(employee.getId());
        } catch (Exception e) {

            System.err.println("Error while fetching follow company list: " + e.getMessage());
            e.printStackTrace();

            return new ArrayList<>();
        }
    }


    @Transactional
    public ResponseEntity<ResponseDto<Object>> deleteFollowCompany(Long id) {

        String userName = authenticationService.getUserFromContext();

        Optional<User> user = userRepository.findByUsername(userName);

        Employee employee = employeeRepository.findByUserId(user.get().getId());

        if (employee == null) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            followCompanyRepository.deleteByEmployeeIdAndCompanyId(employee.getId(), id);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("delete.follow.company.success"),
                    StatusCodeEnum.SAVE1003
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("delete.follow.company.failed"),
                    StatusCodeEnum.SAVE0003
            );
        }
    }
}
