package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.ExperienceRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/experience")
public class ExperienceController {

    private final ExperienceService experienceService;

    @PostMapping()
    public ResponseEntity<ResponseDto<Object>> createExperience(@Valid @RequestBody ExperienceRequest experienceRequest) {
        return experienceService.createExperience(experienceRequest);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<Object>> updateExperience(@PathVariable Long id, @Valid @ModelAttribute ExperienceRequest experienceRequest) {
        return experienceService.updateExperience(id, experienceRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> getExperience(@PathVariable Long id) {
        return experienceService.getExperience(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> deleteExperience(@PathVariable Long id) {
        return experienceService.deleteExperience(id);
    }
}
