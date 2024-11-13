package com.ptit.hirex.controller;

import com.ptit.data.entity.Job;
import com.ptit.hirex.dto.request.JobRequest;
import com.ptit.hirex.dto.request.JobSearchRequest;
import com.ptit.hirex.dto.response.JobResponse;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {
    private final JobService jobService;

    @PostMapping()
    public ResponseEntity<ResponseDto<Object>> createJob(@Valid @RequestBody JobRequest jobRequest) {
        System.out.println(jobRequest);
        return jobService.createJob(jobRequest);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> updateJob(@PathVariable Long id, @Valid @RequestBody JobRequest jobRequest) {
        return jobService.updateJob(id, jobRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> getJob(@PathVariable Long id) {
        return jobService.getJob(id);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseDto<Object>> getJobWith(@PathVariable Long id) {
        return jobService.getJobWith(id);
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
        jobService.deleteJob(id);
        return jobService.deleteJob(id);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDto<Object>> searchJobs(
            JobSearchRequest request
    ) {
        return  jobService.searchJobs(request);
    }
}