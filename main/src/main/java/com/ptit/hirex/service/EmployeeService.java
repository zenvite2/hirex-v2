package com.ptit.hirex.service;

import com.ptit.data.entity.Employee;
import com.ptit.data.entity.User;
import com.ptit.data.repository.EmployeeRepository;
import com.ptit.data.repository.RoleRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.EmployeeDTO;
import com.ptit.hirex.dto.request.EmployeeRequest;
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
    private final ModelMapper modelMapper;

    public ResponseEntity<ResponseDto<Object>> createEmployee(EmployeeRequest employeeRequest) {
        try {
            String username = employeeRequest.getUsername();

            if (userRepository.existsByUsername(username)) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.signup.user.exists"),
                        StatusCodeEnum.AUTH0019
                );
            }

            if (!employeeRequest.getPassword().equals(employeeRequest.getRetryPassword())) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.signup.password.mismatch"),
                        StatusCodeEnum.AUTH0024
                );
            }

            User newUser = User.builder()
                    .email(employeeRequest.getEmail())
                    .username(employeeRequest.getUsername())
                    .password(passwordEncoder.encode(employeeRequest.getPassword()))
                    .role(roleRepo.findById(1L).get())
                    .build();

            User userSave = userRepository.save(newUser);

            Employee employee = new Employee();
            employee.setUserId(userSave.getId());

            employeeRepository.save(employee);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.employee.success"),
                    userSave,
                    StatusCodeEnum.EMPLOYEE1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.employee.failed"),
                    StatusCodeEnum.EMPLOYEE0000
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getEmployee() {
        String userName = authenticationService.getUserFromContext();

        Optional<User> user = userRepository.findByUsername(userName);

        if (user.isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.signup.user.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            Employee employee = employeeRepository.findByUserId(user.get().getId());

            if (employee == null) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("employee.not.found"),
                        StatusCodeEnum.EMPLOYEE4000
                );
            }

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.employee.success"),
                    employee,
                    StatusCodeEnum.EMPLOYEE1001
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.employee.failed"),
                    StatusCodeEnum.EMPLOYEE0001
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> updateEmployee(EmployeeDTO employeeDTO) {

        String userName = authenticationService.getUserFromContext();

        Optional<User> user = userRepository.findByUsername(userName);

        if (user.isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.signup.user.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            Employee employee = employeeRepository.findByUserId(user.get().getId());

            if (employee == null) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("employee.not.found"),
                        StatusCodeEnum.EMPLOYEE4000
                );
            }

            modelMapper.map(employeeDTO, employee);

            if (employeeDTO.getAvatar() != null && !employeeDTO.getAvatar().isEmpty()) {
                String avatar = fileService.uploadImageFile(employeeDTO.getAvatar(), employee.getAvatar(), "AVATAR");
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

            Employee updatedEmployee = employeeRepository.save(employee);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.employee.success"),
                    updatedEmployee,
                    StatusCodeEnum.EMPLOYEE1002
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("update.employee.failed"),
                    StatusCodeEnum.EMPLOYEE0002
            );
        }
    }
}
