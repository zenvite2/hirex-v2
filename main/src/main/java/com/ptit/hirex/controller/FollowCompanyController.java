package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.FollowCompanyRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.FollowCompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('EMPLOYEE')")
@RequestMapping("/follow-company")
public class FollowCompanyController {

    private final FollowCompanyService followCompanyService;

    @PostMapping()
    public ResponseEntity<ResponseDto<Object>> createFollowCompany(@Valid @RequestBody FollowCompanyRequest followCompanyRequest) {
        return followCompanyService.followCompany(followCompanyRequest);
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDto<Object>> getFollowCompany() {
        return followCompanyService.getFollowCompany();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> deleteFollowCompany(@PathVariable Long id) {
        return followCompanyService.deleteFollowCompany(id);
    }
}
