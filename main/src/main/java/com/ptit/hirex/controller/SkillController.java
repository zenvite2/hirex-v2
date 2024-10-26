package com.ptit.hirex.controller;

import com.ptit.data.entity.Skill;
import com.ptit.hirex.dto.request.SkillRequest;
import com.ptit.hirex.dto.response.SkillResponse;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/list")
    public ResponseEntity<ResponseDto<List<SkillResponse>>> getSkill() {
        return skillService.getAllSkill();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> deleteSkill(@PathVariable Long id) {
        return skillService.deleteSkill(id);
    }
}
