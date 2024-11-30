package com.ptit.hirex.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptit.data.dto.SalaryDto;
import com.ptit.hirex.dto.request.JobRequest;
import com.ptit.hirex.dto.request.JobSearchRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {
    private final JobService jobService;

    @PostMapping()
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ResponseDto<Object>> createJob(@Valid @RequestBody JobRequest jobRequest) {
        return jobService.createJob(jobRequest);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ResponseDto<Object>> updateJob(@PathVariable Long id, @Valid @RequestBody JobRequest jobRequest) {
        return jobService.updateJob(id, jobRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> getJob(@PathVariable Long id) {
        return jobService.getJob(id);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseDto<Object>> getJobWith(@PathVariable Long id) {
        return jobService.getJobDetail(id);
    }

    @GetMapping()
    public ResponseEntity<ResponseDto<Object>> getListJob() {
        return jobService.getAllJob();
    }

    @GetMapping("/with-company")
    public ResponseEntity<ResponseDto<Object>> getJobsWithCompany() {
        return jobService.getAllJobsWithCompany();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> deleteJob(@PathVariable Long id) {
        return jobService.deleteJob(id);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchJobs(
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) Long city,
            @RequestParam(required = false) List<Long> industryIds,
            @RequestParam(required = false) List<Long> positionIds,
            @RequestParam(required = false) List<Long> experienceIds,
            @RequestParam(required = false) List<Long> educationIds,
            @RequestParam(required = false) List<Long> jobTypeIds,
            @RequestParam(required = false) String salaryOptions,
            @RequestParam(required = false) List<Long> contractTypeIds,
            Pageable pageable
    ) {
        List<SalaryDto> parsedSalaryOptions = parseSalaryOptions(salaryOptions);

        JobSearchRequest searchRequest = JobSearchRequest.builder()
                .searchQuery(searchQuery)
                .city(city)
                .industryIds(industryIds)
                .positionIds(positionIds)
                .experienceIds(experienceIds)
                .educationIds(educationIds)
                .jobTypeIds(jobTypeIds)
                .salaryOptions(parsedSalaryOptions)
                .contractTypeIds(contractTypeIds)
                .build(); // Không cần truyền page và size nữa

        return jobService.searchJobs(searchRequest, pageable);
    }

    private List<SalaryDto> parseSalaryOptions(String salaryOptionsJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(
                    URLDecoder.decode(salaryOptionsJson, StandardCharsets.UTF_8),
                    new TypeReference<List<SalaryDto>>() {
                    }
            );
        } catch (Exception e) {
            return null;
        }
    }
}