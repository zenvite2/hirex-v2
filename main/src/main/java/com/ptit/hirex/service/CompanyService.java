package com.ptit.hirex.service;

import com.ptit.data.entity.Company;
import com.ptit.data.entity.Employer;
import com.ptit.data.entity.User;
import com.ptit.data.repository.CompanyRepository;
import com.ptit.data.repository.EmployerRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.request.CompanyRequest;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final LanguageService languageService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<ResponseDto<Object>> getCompany() {

        String userName = authenticationService.getUserFromContext();

        Optional<User> user = userRepository.findByUsername(userName);

        if (user.isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.signup.user.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        Employer employer = employerRepository.findByUserId(user.get().getId());

        if (employer == null) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employer.not.found"),
                    StatusCodeEnum.EMPLOYEE4000
            );
        }

        try {
            Optional<Company> company = companyRepository.findById(employer.getCompany());

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.company.success"),
                    company,
                    StatusCodeEnum.COMPANY1001
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.company.failed"),
                    StatusCodeEnum.COMPANY0001
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> updateCompany(CompanyRequest companyRequest) {
        String userName = authenticationService.getUserFromContext();

        Optional<User> userOpt = userRepository.findByUsername(userName);

        if (userOpt.isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.signup.user.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        User user = userOpt.get();
        Employer employer = employerRepository.findByUserId(user.getId());

        if (employer == null) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employer.not.found"),
                    StatusCodeEnum.EMPLOYEE4000
            );
        }

        Optional<Company> companyOpt = companyRepository.findById(employer.getCompany());

        if (companyOpt.isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("company.not.found"),
                    StatusCodeEnum.COMPANY0002
            );
        }

        Company company = companyOpt.get();

        try {
            modelMapper.map(companyRequest, company);
            companyRepository.save(company);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.company.success"),
                    company,
                    StatusCodeEnum.COMPANY1002
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("update.company.failed"),
                    StatusCodeEnum.COMPANY0003
            );
        }
    }

}
