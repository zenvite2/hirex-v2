package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.EducationRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.EducationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/education")
public class EducationController {

    private final EducationService educationService;

    @PostMapping()
    public ResponseEntity<ResponseDto<Object>> createEducation(@Valid @RequestBody EducationRequest educationRequest) {
        return educationService.createEducation(educationRequest);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> updateEducation(@PathVariable Long id, @Valid @RequestBody EducationRequest educationRequest) {
        return educationService.updateEducation(id, educationRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> getEducation(@PathVariable Long id) {
        return educationService.getEducation(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> deleteEducation(@PathVariable Long id) {
        return educationService.deleteEducation(id);
    }
}
