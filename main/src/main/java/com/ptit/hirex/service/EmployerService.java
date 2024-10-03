package com.ptit.hirex.service;

import com.ptit.data.base.User;
import com.ptit.data.entity.Company;
import com.ptit.data.entity.Employer;
import com.ptit.data.repository.CompanyRepository;
import com.ptit.data.repository.EmployerRepository;
import com.ptit.data.repository.RoleRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.request.EmployerRequest;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
    private final AuthenticationService authenticationService;
    private final CompanyRepository companyRepository;
    private final LanguageService languageService;
    private final FileService fileService;


    public ResponseEntity<ResponseDto<Object>> createEmployer(EmployerRequest employerRequest) {
        try {
            String email = authenticationService.getUserFromContext();
            Optional<User> user = userRepository.findByEmail(email);

            user.get().setRole(roleRepo.findById(2L).get());

            userRepository.save(user.get());

            Long companyId;

            if(!companyRepository.existsByName(employerRequest.getNameCompany())){
                Company company = new Company();
                company.setName(employerRequest.getNameCompany());
                company.setAddressId(employerRequest.getAddress());
                company.setWebsite(employerRequest.getWebsite());
                company.setDescription(employerRequest.getDescription());
                companyRepository.save(company);
                companyId = company.getId();
            }else{
                companyId = companyRepository.findByName(employerRequest.getNameCompany()).getId();
            }

            Employer employer = new Employer();
            employer.setUserId(user.get().getId());
            employer.setCompanyId(companyId);
            employer.setPhoneNumber(employerRequest.getPhoneNumber());

            if (employerRequest.getAvatar() != null && !employerRequest.getAvatar().isEmpty()) {
                String avatar = fileService.uploadImageFile(employerRequest.getAvatar(), employer.getAvatar(), String.valueOf(user.get().getId()), "AVATAR");
                if (avatar == null) {
                    log.error("Upload file image avatar failed");
                    return ResponseBuilder.badRequestResponse(
                            languageService.getMessage("upload.file.avatar.failed"),
                            StatusCodeEnum.UPLOADFILE0001
                    );
                } else {
                    employer.setAvatar(publicUrl + "/" + avatar);
                }
            }

            employerRepository.save(employer);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.employer.success"),
                    employer,
                    StatusCodeEnum.EMPLOYER1000
            );
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.employer.failed"),
                    StatusCodeEnum.EMPLOYER0000
            );
        }
    }
}
