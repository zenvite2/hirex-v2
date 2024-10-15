package com.ptit.hirex.service;

import com.ptit.data.entity.Company;
import com.ptit.data.entity.Employer;
import com.ptit.data.entity.User;
import com.ptit.data.repository.CompanyRepository;
import com.ptit.data.repository.EmployerRepository;
import com.ptit.data.repository.RoleRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.EmployeeDTO;
import com.ptit.hirex.dto.request.EmployerRequest;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployerService {

    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepo;
    private final CompanyRepository companyRepository;
    private final LanguageService languageService;
    private final PasswordEncoder passwordEncoder;


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

            if (!companyRepository.existsByCompanyName(employerRequest.getNameCompany())) {
                Company company = new Company();
                company.setCompanyName(employerRequest.getNameCompany());
//                company.setAddress(employerRequest.());
                companyRepository.save(company);
                companyId = company.getId();
            } else {
                companyId = companyRepository.findByCompanyName(employerRequest.getNameCompany()).getId();
            }

            Employer employer = new Employer();
            employer.setUserId(userSave.getId());
            employer.setCompanyId(companyId);

            employerRepository.save(employer);
//            if (employerRequest.getAvatar() != null && !employerRequest.getAvatar().isEmpty()) {
//                String avatar = fileService.uploadImageFile(employerRequest.getAvatar(), employer.getAvatar(), String.valueOf(user.get().getId()), "AVATAR");
//                if (avatar == null) {
//                    log.error("Upload file image avatar failed");
//                    return ResponseBuilder.badRequestResponse(
//                            languageService.getMessage("upload.file.avatar.failed"),
//                            StatusCodeEnum.UPLOADFILE0001
//                    );
//                } else {
//                    employer.setAvatar(publicUrl + "/" + avatar);
//                }
//            }

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

    public ResponseEntity<ResponseDto<Object>> updateEmployer(Long id, EmployeeDTO employeeDTO) {
        return null;
    }
}
