package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.EmployerRequest;
import com.ptit.hirex.model.ApiResponse;
import com.ptit.hirex.service.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employer")
public class EmployerController {

    private final EmployerService employerService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createEmployer(@RequestBody EmployerRequest employer) {

        employerService.createEmployer(employer);

        return ResponseEntity.ok(ApiResponse.builder().message("success").build());
    }
}
