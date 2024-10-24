package com.ptit.hirex.service;

import com.ptit.data.entity.*;
import com.ptit.data.repository.*;
import com.ptit.hirex.dto.request.JobRequest;
import com.ptit.hirex.dto.response.JobResponse;
import com.ptit.hirex.dto.response.JobWithCompanyResponse;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final LanguageService languageService;
    private final ModelMapper modelMapper;
    private final EmployerRepository employerRepository;
    private final DistrictRepository districtRepository;
    private final CityRepository cityRepository;
    private final CompanyRepository companyRepository;

    public ResponseEntity<ResponseDto<Object>> createJob(JobRequest jobRequest) {

        String userName = authenticationService.getUserFromContext();

        Optional<User> optionalUser = userRepository.findByUsername(userName);

        if (optionalUser.isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.signup.user.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        User user = optionalUser.get();

        Employer employer = employerRepository.findByUserId(user.getId());
        if (employer == null) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employer.not.found"),
                    StatusCodeEnum.EMPLOYER4000
            );
        }

        try {
            Job job = modelMapper.map(jobRequest, Job.class);
            job.setEmployer(employer.getId());

            jobRepository.save(job);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.job.success"),
                    job,
                    StatusCodeEnum.JOB1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.job.failed"),
                    StatusCodeEnum.JOB0000
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> updateJob(Long id, JobRequest jobRequest) {
        try {
            Job job = jobRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Not found job"));

            if (jobRequest != null) {
                modelMapper.map(jobRequest, job);
            }

            jobRepository.save(job);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.job.success"),
                    job,
                    StatusCodeEnum.JOB1002
            );
        } catch (NoSuchElementException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("not.found.job"),
                    StatusCodeEnum.JOB4000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("update.job.failed"),
                    StatusCodeEnum.JOB0002
            );
        }
    }


    public ResponseEntity<ResponseDto<Object>> getJob(Long id) {
        try {
            Job job = jobRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(languageService.getMessage("not.found.job")));

            // Lấy thông tin district
            String districtName = districtRepository.findById(job.getDistrict())
                    .map(District::getName)
                    .orElse("");

            // Lấy thông tin city
            String cityName = cityRepository.findById(job.getCity())
                    .map(City::getName)
                    .orElse("");

            // Lấy thông tin employer và company
            Employer employer = employerRepository.findById(job.getEmployer())
                    .orElse(null);

            Company company = null;
            if (employer != null) {
                company = companyRepository.findById(employer.getId())
                        .orElse(null);
            }

            JobWithCompanyResponse jobResponse = JobWithCompanyResponse.builder()
                    .id(job.getId())
                    .title(job.getTitle())
                    .location(job.getLocation())
                    .district(districtName)
                    .city(cityName)
                    .deadline(job.getDeadline())
                    .createdAt(job.getCreatedAt())
                    .companyName(company != null ? company.getCompanyName() : null)
                    .companyLogo(company != null ? company.getLogo() : null)
                    .companyDescription(company != null ? company.getDescription() : null)
                    .build();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.job.success"),
                    jobResponse,
                    StatusCodeEnum.JOB1001
            );
        } catch (NoSuchElementException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("not.found.job"),
                    StatusCodeEnum.JOB4000
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.job.failed"),
                    StatusCodeEnum.JOB0001
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getAllJob() {
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

            Employer employer = employerRepository.findByUserId(user.getId());
            if (employer == null) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("employer.not.found"),
                        StatusCodeEnum.EMPLOYER4000
                );
            }

            List<Job> jobEntities = jobRepository.findAllByEmployer(employer.getId());
            List<JobResponse> jobs = jobEntities.stream()
                    .map(job -> {
                        String districtName = districtRepository.findById(job.getDistrict())
                                .map(District::getName)
                                .orElse("");

                        String cityName = cityRepository.findById(job.getCity())
                                .map(City::getName)
                                .orElse("");

                        return JobResponse.builder()
                                .id(job.getId())
                                .title(job.getTitle())
                                .location(job.getLocation())
                                .district(districtName)
                                .city(cityName)
                                .deadline(job.getDeadline())
                                .createdAt(job.getCreatedAt())
                                .build();
                    })
                    .collect(Collectors.toList());

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.all.jobs.success"),
                    jobs,
                    StatusCodeEnum.JOB1001
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.all.jobs.failed"),
                    StatusCodeEnum.JOB0001
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getAllJobsWithCompany() {
        try {
            // Lấy tất cả job từ database
            List<Job> jobEntities = jobRepository.findAll();

            System.out.println("size: " + jobEntities.size());

            List<JobWithCompanyResponse> jobs = jobEntities.stream()
                    .map(job -> {
                        // Lấy thông tin district
                        String districtName = districtRepository.findById(job.getDistrict())
                                .map(District::getName)
                                .orElse("");

                        // Lấy thông tin city
                        String cityName = cityRepository.findById(job.getCity())
                                .map(City::getName)
                                .orElse("");

                        // Lấy thông tin employer và company
                        Employer employer = employerRepository.findById(job.getEmployer())
                                .orElse(null);

                        Company company = null;
                        if (employer != null) {
                            company = companyRepository.findById(employer.getId())
                                    .orElse(null);
                        }

                        return JobWithCompanyResponse.builder()
                                .id(job.getId())
                                .title(job.getTitle())
                                .location(job.getLocation())
                                .district(districtName)
                                .city(cityName)
                                .deadline(job.getDeadline())
                                .createdAt(job.getCreatedAt())
                                .companyName(company != null ? company.getCompanyName() : null)
                                .companyLogo(company != null ? company.getLogo() : null)
                                .companyDescription(company != null ? company.getDescription() : null)
                                .build();
                    })
                    .collect(Collectors.toList());

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.all.jobs.success"),
                    jobs,
                    StatusCodeEnum.JOB1001
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.all.jobs.failed"),
                    StatusCodeEnum.JOB0001
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> deleteJob(Long id) {
        try {
            Job job = jobRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(languageService.getMessage("not.found.job")));

            jobRepository.deleteById(id);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("delete.job.success"),
                    StatusCodeEnum.JOB1003
            );
        } catch (NoSuchElementException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("not.found.job"),
                    StatusCodeEnum.JOB4000
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("delete.job.failed"),
                    StatusCodeEnum.JOB0003
            );
        }
    }
}