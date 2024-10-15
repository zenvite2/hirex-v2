package com.ptit.hirex.service;

import com.ptit.data.entity.Employee;
import com.ptit.data.entity.User;
import com.ptit.data.entity.Company;
import com.ptit.data.entity.Employer;
import com.ptit.data.repository.CompanyRepository;
import com.ptit.data.repository.EmployerRepository;
import com.ptit.data.repository.RoleRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.request.EmployerRequest;
import com.ptit.hirex.dto.request.EmployerUpdateRequest;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployerService {
    @Value("${minio.url.public}")
    private String publicUrl;

    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepo;
    private final CompanyRepository companyRepository;
    private final LanguageService languageService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final AuthenticationService authenticationService;

    public ResponseEntity<ResponseDto<Object>> createEmployer(EmployerRequest employerRequest) {
        try {

            String email = employerRequest.getEmail();

            if (userRepository.existsByEmail(email)) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.signup.email.exists"),
                        StatusCodeEnum.AUTH0019
                );
            }

            if (!employerRequest.getPassword().equals(employerRequest.getRetryPassword())) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.signup.password.mismatch"),
                        StatusCodeEnum.AUTH0024
                );
            }

            User newUser = User.builder()
                    .email(employerRequest.getEmail())
                    .username(employerRequest.getUsername())
                    .password(passwordEncoder.encode(employerRequest.getPassword()))
                    .role(roleRepo.findById(2L).get())
                    .build();

            User userSave = userRepository.save(newUser);

            Long companyId;

            if (employerRequest.getCompanyId() == null) {
                if (!companyRepository.existsByCompanyName(employerRequest.getNameCompany())) {
                    Company company = new Company();
                    company.setCompanyName(employerRequest.getNameCompany());
                    company.setCity(employerRequest.getCity());
                    company.setDistrict(employerRequest.getDistrict());
                    companyRepository.save(company);
                    companyId = company.getId();
                } else {
                    companyId = companyRepository.findByCompanyName(employerRequest.getNameCompany()).getId();
                }
            } else {
                companyId = employerRequest.getCompanyId();
            }

            Employer employer = new Employer();
            employer.setUserId(userSave.getId());
            employer.setCompany(companyId);
            employer.setEmail(employerRequest.getEmail());
            employer.setPhoneNumber(employerRequest.getPhoneNumber());
            employer.setGender(employerRequest.getGender());

            employerRepository.save(employer);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.employer.success"),
                    userSave,
                    StatusCodeEnum.EMPLOYER1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.employer.failed"),
                    StatusCodeEnum.EMPLOYER0000
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getEmployer() {
        String userName = authenticationService.getUserFromContext();

        Optional<User> user = userRepository.findByUsername(userName);

        if (user.isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.signup.user.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            Employer employer = employerRepository.findByUserId(user.get().getId());

            if (employer == null) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("employer.not.found"),
                        StatusCodeEnum.EMPLOYEE4000
                );
            }

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.employer.success"),
                    employer,
                    StatusCodeEnum.EMPLOYEE1001
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.employer.failed"),
                    StatusCodeEnum.EMPLOYEE0001
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> updateEmployer(EmployerUpdateRequest employerUpdateRequest) {

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
                    StatusCodeEnum.EMPLOYER4000
            );
        }

        try {
            modelMapper.map(employer, employerUpdateRequest);

            if (employerUpdateRequest.getAvatar() != null && !employerUpdateRequest.getAvatar().isEmpty()) {
                String avatar = fileService.uploadImageFile(employerUpdateRequest.getAvatar(), employer.getAvatar(), String.valueOf(user.get().getId()), "AVATAR");
                if (avatar == null) {
                    log.error("Upload file image avatar failed");
                    return ResponseBuilder.badRequestResponse(
                            languageService.getMessage("upload.file.avatar.failed"),
                            StatusCodeEnum.UPLOADFILE0001
                    );
                } else {
                    employer.setAvatar(publicUrl + "/" + avatar);
                }
            } else {
                employer.setAvatar(employer.getAvatar());
            }

            employerRepository.save(employer);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.employer.success"),
                    employer,
                    StatusCodeEnum.EMPLOYER1002
            );

        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("update.employer.failed"),
                    StatusCodeEnum.EMPLOYER0002
            );
        }
    }
}
