package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.SavedJobRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.SavedJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/saved-job")
public class SavedJobController {

    private final SavedJobService savedJobService;

    @PostMapping()
    public ResponseEntity<ResponseDto<Object>> createSavedJob(@Valid @RequestBody SavedJobRequest savedJobRequest) {
        return savedJobService.savedJob(savedJobRequest);
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDto<Object>> getSavedJob() {
        return savedJobService.getSavedJob();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> deleteSavedJob(@PathVariable Long id) {
        return savedJobService.deleteSavedJob(id);
    }
}
