package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.JobRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {
    private final JobService jobService;

    @PostMapping()
    public ResponseEntity<ResponseDto<Object>> createJob(@Valid @RequestBody JobRequest jobRequest) {
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

    @GetMapping()
    public ResponseEntity<ResponseDto<Object>> getListJob() {
        return jobService.getAllJob();
    }

    @GetMapping("/with-company")
    public ResponseEntity<ResponseDto<Object>> getJobsWithCompany() {
        return jobService.getAllJobsWithCompany();
    }
}
