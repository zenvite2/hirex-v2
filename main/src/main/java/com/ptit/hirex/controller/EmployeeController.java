package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.EmployeeRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
