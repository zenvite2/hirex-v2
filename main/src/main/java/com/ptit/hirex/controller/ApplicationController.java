package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.ApplicationRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping()
    public ResponseEntity<ResponseDto<Object>> createApplication(@Valid @RequestBody ApplicationRequest applicationRequest) {
        return applicationService.createApplication(applicationRequest);
    }
}
