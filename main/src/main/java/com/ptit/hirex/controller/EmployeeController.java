package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.EmployeeRequest;
import com.ptit.hirex.model.ApiResponse;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.EmployeeService;
import com.ptit.security.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<Object>> createEmployee(@Valid @RequestBody EmployeeRequest employee) {

        return employeeService.createEmployee(employee);
    }
}
