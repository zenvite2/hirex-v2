package com.ptit.hirex.service;

import com.ptit.data.entity.Employee;
import com.ptit.data.entity.Job;
import com.ptit.data.entity.SavedJob;
import com.ptit.data.repository.EmployeeRepository;
import com.ptit.data.repository.JobRepository;
import com.ptit.data.repository.SavedJobRepository;
import com.ptit.hirex.dto.request.SavedJobRequest;
import com.ptit.hirex.dto.response.JobResponse;
import com.ptit.hirex.dto.response.SavedJobResponse;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final ModelMapper modelMapper;
    private final LanguageService languageService;
    private final AuthenticationService authenticationService;
    private final JobRepository jobRepository;
    private final EmployeeRepository employeeRepository;

    public ResponseEntity<ResponseDto<Object>> savedJob(SavedJobRequest savedJobRequest) {
        Employee employee = employeeRepository.findById(savedJobRequest.getEmployeeId()).get();

        if (employee == null) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            Optional<Job> optionalJob = jobRepository.findById(savedJobRequest.getJobId());
            if (optionalJob.isEmpty()) {
                return ResponseBuilder.okResponse(
                        languageService.getMessage("save.job.not.found"),
                        StatusCodeEnum.SAVE0002
                );
            }

            SavedJob savedJob = SavedJob.builder()
                    .jobId(savedJobRequest.getJobId())
                    .employeeId(savedJobRequest.getEmployeeId())
                    .build();

            savedJobRepository.save(savedJob);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.save.job.success"),
                    savedJob,
                    StatusCodeEnum.SAVE1000
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.save.job.failed"),
                    StatusCodeEnum.SAVE0000
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getSavedJob() {
        Employee employee = authenticationService.getEmployeeFromContext();

        if (employee == null) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employee.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            List<SavedJob> savedJobList = savedJobRepository.findAllByEmployeeId(employee.getId());

            List<SavedJobResponse> jobResponses = new ArrayList<>();

            for (SavedJob savedJob : savedJobList) {
                SavedJobResponse savedJobResponse = modelMapper.map(savedJob, SavedJobResponse.class);

                Optional<Job> optionalJob = jobRepository.findById(savedJob.getJobId());
                if (optionalJob.isEmpty()) {
                    continue;
                }
                Job job = optionalJob.get();

                JobResponse jobResponse = JobResponse.builder()
                        .id(job.getId())
                        .title(job.getTitle())
                        .location(job.getLocation())
                        .minSalary(job.getMinSalary())
                        .maxSalary(job.getMaxSalary())
                        .deadline(job.getDeadline())
                        .createdAt(job.getCreatedAt())
                        .build();
                savedJobResponse.setJobResponse(jobResponse);

                jobResponses.add(savedJobResponse);
            }

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.save.job.success"),
                    jobResponses,
                    StatusCodeEnum.SAVE1002
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.save.job.failed"),
                    StatusCodeEnum.SAVE0002
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> deleteSavedJob(Long id) {

        try {
            savedJobRepository.deleteById(id);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("delete.save.job.success"),
                    StatusCodeEnum.SAVE1003
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("delete.save.job.failed"),
                    StatusCodeEnum.SAVE0003
            );
        }
    }
}
