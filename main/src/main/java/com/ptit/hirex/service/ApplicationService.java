package com.ptit.hirex.service;

import com.ptit.data.entity.*;
import com.ptit.data.enums.ApplicationStatus;
import com.ptit.data.repository.*;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final NotificationService notificationService;
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
    private final EmployerRepository employerRepository;

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

            Optional<Employer> employer = employerRepository.findById(job.get().getEmployer());

            //tao thong bao
            notificationService.createNotification(employer.get().getUserId(), applicationRequest.getJobId(), "APPLY");

            Application application = Application.builder()
                    .jobId(applicationRequest.getJobId())
                    .employeeId(employee.getId())
                    .coverLetter(applicationRequest.getCoverLetter())
                    .resumeId(applicationRequest.getResumeId() != null ? applicationRequest.getResumeId() : null)
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
        try {
            List<Application> applications = applicationRepository.findAll();

            if (applications.isEmpty()) {
                return ResponseBuilder.okResponse(
                        "No applications found",
                        StatusCodeEnum.APPLICATION1000
                );
            }

            List<ApplicationResponse> applicationResponses = applications.stream()
                    .map(application -> {
                        Job job = jobRepository.findById(application.getJobId()).orElse(null);
                        if (job == null) {
                            log.error("Job not found for id: " + application.getJobId());
                            return null;
                        }

                        Employee employee = employeeRepository.findById(application.getEmployeeId()).orElse(null);
                        if (employee == null) {
                            log.error("Employee not found for id: " + application.getEmployeeId());
                            return null;
                        }

                        User user = userRepository.findById(employee.getUserId()).orElse(null);
                        if (user == null) {
                            log.error("User not found for id: " + employee.getUserId());
                            return null;
                        }

                        return ApplicationResponse.builder()
                                .id(application.getId())
                                .jobId(job.getId())
                                .userId(user.getId())
                                .avatar(user.getAvatar() != null ? user.getAvatar() : "No avatar provided")
                                .jobTitle(job.getTitle())
                                .address(job.getLocation())
                                .cvPdf(application.getCvPdf() != null ? application.getCvPdf() : "No CV provided")
                                .resumeId(application.getResumeId())
                                .employeeId(employee.getId())
                                .fullName(user.getFullName() != null ? user.getFullName() : "No name provided")
                                .coverLetter(application.getCoverLetter())
                                .status(application.getStatus())
                                .createdAt(application.getCreatedAt())
                                .username(user.getUsername())
                                .build();
                    })
                    .filter(Objects::nonNull) // Loại bỏ null khỏi danh sách
                    .collect(Collectors.toList());

            return ResponseBuilder.okResponse(
                    "get list success",
                    applicationResponses,
                    StatusCodeEnum.APPLICATION1000
            );

        } catch (Exception e) {
            log.error("Error while fetching applications", e); // Log lỗi
            return ResponseBuilder.badRequestResponse(
                    "get list failed: " + e.getMessage(),
                    StatusCodeEnum.APPLICATION1000
            );
        }
    }

    public ResponseEntity<ResponseDto<List<ApplicationResponse>>> getApplicationByUserId(Long userId) {
        try {

            Employee employeeOpt = employeeRepository.findByUserId(userId);

            List<Application> applications = applicationRepository.findByEmployeeId(employeeOpt.getId());

            if (applications.isEmpty()) {
                return ResponseBuilder.okResponse(
                        "No applications found",
                        StatusCodeEnum.APPLICATION1000
                );
            }

            List<ApplicationResponse> applicationResponses = applications.stream()
                    .map(application -> {
                        Job job = jobRepository.findById(application.getJobId()).orElse(null);
                        if (job == null) {
                            log.error("Job not found for id: " + application.getJobId());
                            return null; // Hoặc ném lỗi tùy trường hợp
                        }

                        Employee employee = employeeRepository.findById(application.getEmployeeId()).orElse(null);
                        if (employee == null) {
                            log.error("Employee not found for id: " + application.getEmployeeId());
                            return null; // Hoặc ném lỗi tùy trường hợp
                        }

                        User user = userRepository.findById(employee.getUserId()).orElse(null);
                        if (user == null) {
                            log.error("User not found for id: " + employee.getUserId());
                            return null; // Hoặc ném lỗi tùy trường hợp
                        }

                        return ApplicationResponse.builder()
                                .id(application.getId())
                                .jobId(job.getId())
                                .jobTitle(job.getTitle())
                                .address(job.getLocation())
                                .cvPdf(application.getCvPdf() != null ? application.getCvPdf() : "No CV provided")
                                .employeeId(employee.getId())
                                .fullName(user.getFullName() != null ? user.getFullName() : "No name provided")
                                .coverLetter(application.getCoverLetter())
                                .status(application.getStatus())
                                .createdAt(application.getCreatedAt())
                                .build();
                    })
                    .filter(Objects::nonNull) // Loại bỏ null khỏi danh sách
                    .collect(Collectors.toList());

            return ResponseBuilder.okResponse(
                    "get list success",
                    applicationResponses,
                    StatusCodeEnum.APPLICATION1000
            );

        } catch (Exception e) {
            log.error("Error while fetching applications", e); // Log lỗi
            return ResponseBuilder.badRequestResponse(
                    "get list failed: " + e.getMessage(),
                    StatusCodeEnum.APPLICATION1000
            );
        }
    }


    public ResponseEntity<ResponseDto<ApplicationResponse>> updateStatus(Long id, ApplicationStatus status) throws MessagingException {
        // Cập nhật status
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ExpressionException("Application not found with id: " + id));
        application.setStatus(status);
        applicationRepository.save(application);

        Optional<Employee> employee = employeeRepository.findById(application.getEmployeeId());
        if (employee.isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("not.found.employee"),
                    StatusCodeEnum.EMPLOYER4000
            );
        }

        //tao thong bao
        notificationService.createNotification(employee.get().getUserId(), application.getJobId(), String.valueOf(application.getStatus()));

        Optional<Job> jobOptional = jobRepository.findById(application.getJobId());
        if (jobOptional.isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("not.found.job"),
                    StatusCodeEnum.JOB4000
            );
        }

        Job job = jobOptional.get();

        Optional<Employer> employer = employerRepository.findById(job.getEmployer());
        if (employer.isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("not.found.employer"),
                    StatusCodeEnum.EMPLOYER4000
            );
        }

        User user = userRepository.findById(employee.get().getUserId()).get();

        // Map sang response
        ApplicationResponse applicationResponse = ApplicationResponse.builder()
                .id(application.getId())
                .jobId(job.getId())
                .jobTitle(job.getTitle())
                .address(job.getLocation())
                .employeeId(employee.get().getId())
                .cvPdf(application.getCvPdf())
                .cvPdf(application.getCvPdf())
                .fullName(user.getFullName())
                .coverLetter(application.getCoverLetter())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .build();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("employeeName", user.getFullName());
        templateModel.put("jobTitle", job.getTitle());
        templateModel.put("employerName", user.getFullName());
        templateModel.put("status", application.getStatus());

        mailService.sendJobApplicationEmail(user.getEmail(), job.getTitle(), templateModel);

        return ResponseBuilder.badRequestResponse(
                "Update status successfully",
                applicationResponse,
                StatusCodeEnum.APPLICATION1000
        );
    }

    public ResponseEntity<ResponseDto<Object>> delete(Long id) {
        try{
            applicationRepository.deleteById(id);
            return ResponseBuilder.badRequestResponse(
                    "Update status successfully",
                    StatusCodeEnum.APPLICATION1000
            );
        }catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    "Update status successfully",
                    StatusCodeEnum.APPLICATION1000
            );
        }
    }

}

