package com.ptit.hirex.controller;

import com.ptit.data.entity.Experience;
import com.ptit.hirex.dto.request.ExperienceRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/list")
    public ResponseEntity<ResponseDto<List<Experience>>> getExperience() {
        return experienceService.getExperience();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseDto<Experience>> getExperience(@PathVariable Long id) {
        return experienceService.getExperienceById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> deleteExperience(@PathVariable Long id) {
        return experienceService.deleteExperience(id);
    }
}
