package com.ptit.hirex.service;

import com.ptit.data.entity.Application;
import com.ptit.data.entity.Employee;
import com.ptit.data.entity.Job;
import com.ptit.data.entity.User;
import com.ptit.data.enums.ApplicationStatus;
import com.ptit.data.repository.ApplicationRepository;
import com.ptit.data.repository.EmployeeRepository;
import com.ptit.data.repository.JobRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.request.ApplicationRequest;
import com.ptit.hirex.dto.response.ApplicationResponse;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ExpressionException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    @Value("${minio.url.public}")
    private String publicUrl;

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final EmployeeRepository employeeRepository;
    private final LanguageService languageService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final MailService mailService;

    public ResponseEntity<ResponseDto<Object>> createApplication(ApplicationRequest applicationRequest) {
        try {
            String userName = authenticationService.getUserFromContext();

            Optional<User> optionalUser = userRepository.findByUsername(userName);

            if (optionalUser.isEmpty()) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.signup.user.not.found"),
                        StatusCodeEnum.AUTH0016
                );
            }

            User user = optionalUser.get();

            Employee employee = employeeRepository.findByUserId(user.getId());
            if (employee == null) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("not.found.employee"),
                        StatusCodeEnum.EMPLOYER4000
                );
            }

            Optional<Job> job = jobRepository.findById(applicationRequest.getJobId());
            if (job.isEmpty()) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("not.found.job"),
                        StatusCodeEnum.JOB4000
                );
            }

            Application application = Application.builder()
                    .jobId(applicationRequest.getJobId())
                    .employeeId(employee.getId())
                    .coverLetter(applicationRequest.getCoverLetter())
                    .status(ApplicationStatus.PENDING)
                    .build();

            if (applicationRequest.getCvPdf() != null && !applicationRequest.getCvPdf().isEmpty()) {
                String cv = fileService.uploadFile(applicationRequest.getCvPdf(), "CV");
                if (cv == null) {
                    log.error("Upload file image avatar failed");
                    return ResponseBuilder.badRequestResponse(
                            languageService.getMessage("upload.file.avatar.failed"),
                            StatusCodeEnum.UPLOADFILE0001
                    );
                } else {
                    application.setCvPdf(publicUrl + "/" + cv);
                }
            }

            applicationRepository.save(application);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("application.create.success"),
                    application,
                    StatusCodeEnum.APPLICATION1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("application.create.failed"),
                    StatusCodeEnum.APPLICATION0001
            );
        }
    }


    public ResponseEntity<ResponseDto<List<ApplicationResponse>>> getAllApplications() {
        List<Application> applications = applicationRepository.findAll();

        List<ApplicationResponse> applicationResponses = applications.stream()
                .map(application -> {
                    Job job = jobRepository.findById(application.getJobId())
                            .orElseThrow(() -> new RuntimeException("Job not found"));
                    Employee employee = employeeRepository.findById(application.getEmployeeId())
                            .orElseThrow(() -> new RuntimeException("Employee not found"));

                    return ApplicationResponse.builder()
                            .id(application.getId())
                            .jobId(job.getId())
                            .jobTitle(job.getTitle())
                            .address(job.getLocation())
                            .employeeId(employee.getId())
                            .fullName(employee.getFullName())
                            .coverLetter(application.getCoverLetter())
                            .status(application.getStatus())
                            .createdAt(application.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseBuilder.badRequestResponse(
                "get list success",
                applicationResponses,
                StatusCodeEnum.APPLICATION1000
        );
    }

    public ResponseEntity<ResponseDto<ApplicationResponse>> updateStatus(Long id, ApplicationStatus status){
        // Cập nhật status
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ExpressionException("Application not found with id: " + id));
        application.setStatus(status);
        applicationRepository.save(application);

        // Lấy thông tin job và employee
        Job job = jobRepository.findById(application.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));
        Employee employee = employeeRepository.findById(application.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Map sang response
        ApplicationResponse applicationResponse = ApplicationResponse.builder()
                .id(application.getId())
                .jobId(job.getId())
                .jobTitle(job.getTitle())
                .address(job.getLocation())
                .employeeId(employee.getId())
                .fullName(employee.getFullName())
                .coverLetter(application.getCoverLetter())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .build();

//        String subject = true ? "Your Job Application Has Been Accepted!" : "Your Job Application Status Update";
//        String status = false ? "accepted" : "rejected";
//
//        Map<String, Object> templateModel = new HashMap<>();
//        templateModel.put("employeeName", employee.getFullName());
//        templateModel.put("jobTitle", job.getTitle());
//        templateModel.put("employerName", "abcd");
//        templateModel.put("status", application.getStatus());
//
//        mailService.sendJobApplicationEmail(employee.getEmail(), job.getTitle(), templateModel);

        return ResponseBuilder.badRequestResponse(
                "Update status successfully",
                applicationResponse,
                StatusCodeEnum.APPLICATION1000
        );
    }

}

