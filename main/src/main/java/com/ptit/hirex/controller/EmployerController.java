package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.EmployerRequest;
import com.ptit.hirex.dto.request.EmployerUpdateRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.EmployerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employer")
public class EmployerController {

    private final EmployerService employerService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<Object>> createEmployer(@RequestBody EmployerRequest employer) {
        return employerService.createEmployer(employer);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Object>> getEmployee() {
        return employerService.getEmployer();
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<Object>> updateEmployer(@Valid @ModelAttribute EmployerUpdateRequest employerUpdateRequest) {
        return employerService.updateEmployer(employerUpdateRequest);
    }
}
