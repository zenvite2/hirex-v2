package com.ptit.hirex.controller;

import com.ptit.hirex.dto.EmployeeDTO;
import com.ptit.hirex.dto.request.EmployeeRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> getEmployee(@PathVariable Long id) {
        return employeeService.getEmployee(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<Object>> updateEmployee(@PathVariable Long id, @Valid @ModelAttribute EmployeeDTO employeeDTO) {
        return employeeService.updateEmployee(id, employeeDTO);
    }
}
