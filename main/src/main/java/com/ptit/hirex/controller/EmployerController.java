package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.EmployerRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employer")
public class EmployerController {

    private final EmployerService employerService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<Object>> createEmployer(@RequestBody EmployerRequest employer) {
        return employerService.createEmployer(employer);
    }
}
