package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.CompanyRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> getCompanyById(@PathVariable("id") Long id) {
        return companyService.getCompanyById(id);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<Object>> updateCompany(@ModelAttribute @Valid CompanyRequest companyRequest) {
        return companyService.updateCompany(companyRequest);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDto<Object>> getAllCompanies() {
        return companyService.getAllCompanies();
    }
}
