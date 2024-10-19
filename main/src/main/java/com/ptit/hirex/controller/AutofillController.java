package com.ptit.hirex.controller;

import com.ptit.data.entity.*;
import com.ptit.hirex.dto.response.DistrictResponse;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.AutofillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auto-fill")
public class AutofillController {
    private final AutofillService autofillService;

    @GetMapping("/city")
    public ResponseEntity<ResponseDto<List<City>>> autofillCity(@RequestParam(required = false) String name) {
        return autofillService.autofillCity(name);
    }

    @GetMapping("/district")
    public ResponseEntity<ResponseDto<List<DistrictResponse>>> autofillDistrict(@RequestParam(required = false) String name, @RequestParam(required = false) Long cityIds) {
        return autofillService.autofillDistrict(name, cityIds);
    }

    @GetMapping("/salary")
    public ResponseEntity<ResponseDto<List<Salary>>> autofillSalary() {
        return autofillService.autofillSalary();
    }

    @GetMapping("/job-type")
    public ResponseEntity<ResponseDto<List<JobType>>> autofillJobType() {
        return autofillService.autofillJobType();
    }

    @GetMapping("/tech")
    public ResponseEntity<ResponseDto<List<Tech>>> autofillTech() {
        return autofillService.autofillTech();
    }

    @GetMapping("/year-experience")
    public ResponseEntity<ResponseDto<List<YearExperience>>> autofillYearExperience() {
        return autofillService.autofillYearExperience();
    }

    @GetMapping("/position")
    public ResponseEntity<ResponseDto<List<Position>>> autofillPosition() {
        return autofillService.autofillPosition();
    }

    @GetMapping("/contract-type")
    public ResponseEntity<ResponseDto<List<ContractType>>> autofillContractType() {
        return autofillService.autofillContractType();
    }

    @GetMapping("/company")
    public ResponseEntity<ResponseDto<List<Company>>> autofillCompany() {
        return autofillService.autofillCompany();
    }

}
