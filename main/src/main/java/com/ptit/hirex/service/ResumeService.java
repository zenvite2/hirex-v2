package com.ptit.hirex.service;

import com.ptit.data.entity.Resume;
import com.ptit.data.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public Resume save(Resume resume) {
        return resumeRepository.save(resume);
    }

    public Resume findById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found with id: " + id));
    }

    public List<Resume> findAll() {
        return resumeRepository.findAll();
    }

    public Resume update(Resume resume) {
        if (!resumeRepository.existsById(resume.getId())) {
            throw new RuntimeException("Resume not found with id: " + resume.getId());
        }
        return resumeRepository.save(resume);
    }

    public void deleteById(Long id) {
        resumeRepository.deleteById(id);
    }
}