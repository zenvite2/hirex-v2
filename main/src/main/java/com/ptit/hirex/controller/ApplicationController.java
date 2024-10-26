package com.ptit.hirex.controller;

import com.ptit.data.enums.ApplicationStatus;
import com.ptit.hirex.dto.request.ApplicationRequest;
import com.ptit.hirex.dto.response.ApplicationResponse;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<Object>> createApplication(@Valid @ModelAttribute ApplicationRequest applicationRequest) {
        return applicationService.createApplication(applicationRequest);
    }

    @GetMapping()
    public ResponseEntity<ResponseDto<List<ApplicationResponse>>> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDto<ApplicationResponse>> updateStatus(
            @PathVariable("id") Long id,
            @RequestParam ApplicationStatus status) {
        return applicationService.updateStatus(id, status);
    }
}
