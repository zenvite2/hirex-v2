package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.SkillRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skill")
public class SkillController {

    private final SkillService skillService;

    @PostMapping()
    public ResponseEntity<ResponseDto<Object>> createSkill(@Valid @RequestBody SkillRequest skillRequest) {
        return skillService.createSkill(skillRequest);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> updateSkill(@PathVariable Long id, @Valid @RequestBody SkillRequest skillRequest) {
        return skillService.updateSkill(id, skillRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> getSkill(@PathVariable Long id) {
        return skillService.getSkill(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> deleteSkill(@PathVariable Long id) {
        return skillService.deleteSkill(id);
    }
}
