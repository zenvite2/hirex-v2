package com.ptit.hirex.service;

import com.ptit.data.base.User;
import com.ptit.data.entity.Employee;
import com.ptit.data.repository.EmployeeRepository;
import com.ptit.data.repository.RoleRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.request.EmployeeRequest;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {
    @Value("${minio.url.public}")
    private String publicUrl;

    private final EmployeeRepository employeeRepository;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepo;
    private final FileService fileService;
    private final LanguageService languageService;


    public ResponseEntity<ResponseDto<Object>> createEmployee(EmployeeRequest employeeRequest) {
        try{
            String email = authenticationService.getUserFromContext();
            Optional<User> user = userRepository.findByEmail(email);

            user.get().setRole(roleRepo.findById(1L).get());

            userRepository.save(user.get());

            Employee employee = new Employee();
            employee.setUserId(user.get().getId());
            employee.setPhoneNumber(employeeRequest.getPhoneNumber());
            employee.setGender(employee.getGender());
            employee.setYearsOfExperience(employeeRequest.getYearsOfExperience());
            employee.setEducationId(employeeRequest.getEducationId());

            if (employeeRequest.getAvatar() != null && !employeeRequest.getAvatar().isEmpty()) {
                String avatar = fileService.uploadImageFile(employeeRequest.getAvatar(), employee.getAvatar(), String.valueOf(user.get().getId()), "AVATAR");
                if (avatar == null) {
                    log.error("Upload file image avatar failed");
                    return ResponseBuilder.badRequestResponse(
                            languageService.getMessage("upload.file.avatar.failed"),
                            StatusCodeEnum.UPLOADFILE0001
                    );
                } else {
                    employee.setAvatar(publicUrl + "/" + avatar);
                }
            }

            if (employeeRequest.getResume() != null && !employeeRequest.getResume().isEmpty()) {
                String resume = fileService.uploadImageFile(employeeRequest.getResume(), employee.getResume(), String.valueOf(user.get().getId()), "RESUME");
                if (resume == null) {
                    log.error("Upload file image resume failed");
                    return ResponseBuilder.badRequestResponse(
                            languageService.getMessage("upload.file.resume.failed"),
                            StatusCodeEnum.UPLOADFILE0001
                    );
                } else {
                    employee.setAvatar(publicUrl + "/" + resume);
                }
            }

            employeeRepository.save(employee);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.employee.success"),
                    employee,
                    StatusCodeEnum.EMPLOYEE1000
            );
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.employee.failed"),
                    StatusCodeEnum.EMPLOYEE0000
            );
        }
    }
}
