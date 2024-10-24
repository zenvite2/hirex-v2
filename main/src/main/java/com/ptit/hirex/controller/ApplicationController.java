package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.ApplicationRequest;
import com.ptit.hirex.dto.response.ApplicationResponse;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping()
    public ResponseEntity<ResponseDto<Object>> createApplication(@Valid @RequestBody ApplicationRequest applicationRequest) {
        return applicationService.createApplication(applicationRequest);
    }

    @GetMapping()
    public ResponseEntity<ResponseDto<List<ApplicationResponse>>> getAllApplications() {
        return applicationService.getAllApplications();
    }
}
