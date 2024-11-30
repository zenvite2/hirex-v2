package com.ptit.hirex.cms;

import com.ptit.hirex.dto.request.JobStatusRequest;
import com.ptit.hirex.dto.request.UserStatusRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.JobService;
import com.ptit.hirex.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/cms")
public class CmsController {
    private final JobService jobService;

    private final UserInfoService userInfoService;

    @GetMapping()
    public ResponseEntity<ResponseDto<Object>> getListJob() {
        return jobService.getAll();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> updateActive(@PathVariable Long id, @RequestBody JobStatusRequest jobStatusRequest) {
        return jobService.updateActiveJob(id, jobStatusRequest);
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseDto<Object>> getListUser() {
        return userInfoService.getAllUser();
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<ResponseDto<Object>> updateUserActive(@PathVariable Long id, @RequestBody UserStatusRequest userStatusRequest) {
        return userInfoService.updateUser(id, userStatusRequest);
    }
}
