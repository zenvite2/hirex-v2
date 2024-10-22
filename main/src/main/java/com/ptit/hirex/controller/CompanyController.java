package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.CompanyRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<ResponseDto<Object>> getCompany() {
        return companyService.getCompany();
    }

    @PatchMapping()
    public ResponseEntity<ResponseDto<Object>> updateCompany(@RequestBody @Valid CompanyRequest companyRequest) {
        return companyService.updateCompany(companyRequest);
    }
}
