package com.ptit.hirex.controller;

import com.ptit.data.entity.Resume;
import com.ptit.hirex.dto.request.ResumeRequest;
import com.ptit.hirex.dto.response.ResumeResponse;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('EMPLOYEE')")
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<ResponseDto<Object>> createResume(@RequestBody ResumeRequest resumeRequest) {
        return resumeService.createResume(resumeRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponse> getResume(@PathVariable Long id) {
        return ResponseEntity.ok(resumeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Object>> getAllResumes() {
        return resumeService.getAllResume();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resume> updateResume(@PathVariable Long id, @RequestBody Resume resume) {
        resume.setId(id);
        return ResponseEntity.ok(resumeService.update(id, resume));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> deleteResume(@PathVariable Long id) {
        return resumeService.deleteResume(id);
    }
}

