package com.ptit.hirex.controller;

import com.ptit.data.entity.Resume;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<ResponseDto<Object>> createResume() {
        return resumeService.createResume();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResume(@PathVariable Long id) {
        return ResponseEntity.ok(resumeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<Object>> getAllResumes() {
        return resumeService.getAllResume();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resume> updateResume(@PathVariable Long id, @RequestBody Resume resume) {
        resume.setId(id);
        return ResponseEntity.ok(resumeService.update(resume));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        resumeService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

