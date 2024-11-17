package com.ptit.hirex.service;

import com.ptit.data.entity.Company;
import com.ptit.data.entity.Employer;
import com.ptit.data.entity.User;
import com.ptit.data.repository.*;
import com.ptit.hirex.dto.request.CompanyRequest;
import com.ptit.hirex.dto.response.CompanyResponse;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final FileService fileService;
    @Value("${minio.url.public}")
    private String publicUrl;

    private final CompanyRepository companyRepository;
    private final LanguageService languageService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;
    private final ModelMapper modelMapper;
    private final ScaleRepository scaleRepository;
    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final JobRepository jobRepository;

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
            Optional<Company> companyOpt = companyRepository.findById(employer.getCompany());

            Company company = companyOpt.get();

            CompanyResponse companyResponse = modelMapper.map(company, CompanyResponse.class);
            companyResponse.setScale(scaleRepository.findById(company.getScale()).get().getName());
            companyResponse.setCity(cityRepository.findById(company.getCity()).get().getName());
            companyResponse.setDistrict(districtRepository.findById(company.getDistrict()).get().getName());
            companyResponse.setJobs(jobRepository.findAllByEmployer(employer.getId()));

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.company.success"),
                    companyResponse,
                    StatusCodeEnum.COMPANY1001
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.company.failed"),
                    StatusCodeEnum.COMPANY0001
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getCompanyById(Long id) {
        try {
            Optional<Company> companyOpt = companyRepository.findById(id);

            Company company = companyOpt.get();

            Optional<Employer> employer = employerRepository.findById(company.getEmployerId());

            CompanyResponse companyResponse = modelMapper.map(company, CompanyResponse.class);
            companyResponse.setScale(scaleRepository.findById(company.getScale()).get().getName());
            companyResponse.setCity(cityRepository.findById(company.getCity()).get().getName());
            companyResponse.setDistrict(districtRepository.findById(company.getDistrict()).get().getName());
            companyResponse.setJobs(jobRepository.findAllByEmployer(employer.get().getId()));

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.company.success"),
                    companyResponse,
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
            company.setEmployerId(employer.getId());
            if (companyRequest.getLogo() != null && !companyRequest.getLogo().isEmpty()) {
                String logo = fileService.uploadImageFile(companyRequest.getLogo(), company.getLogo(), "LOGO");
                if (logo == null) {
                    log.error("Upload file image avatar failed");
                    return ResponseBuilder.badRequestResponse(
                            languageService.getMessage("upload.file.avatar.failed"),
                            StatusCodeEnum.UPLOADFILE0001
                    );
                } else {
                    company.setLogo(publicUrl + "/" + logo);
                }
            }

            companyRepository.save(company);

            CompanyResponse companyResponse = modelMapper.map(company, CompanyResponse.class);
            companyResponse.setScale(scaleRepository.findById(companyRequest.getScale()).get().getName());
            companyResponse.setCity(cityRepository.findById(companyRequest.getCity()).get().getName());
            companyResponse.setDistrict(districtRepository.findById(companyRequest.getDistrict()).get().getName());

            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.company.success"),
                    companyResponse,
                    StatusCodeEnum.COMPANY1002
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("update.company.failed"),
                    StatusCodeEnum.COMPANY0003
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getAllCompanies() {
        try {
            List<Company> companies = companyRepository.findAll();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.company.success"),
                    companies,
                    StatusCodeEnum.COMPANY1001
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.company.failed"),
                    StatusCodeEnum.COMPANY0001
            );
        }
    }
}
