package com.ptit.hirex.cms;

import com.ptit.data.entity.NotificationPattern;
import com.ptit.data.repository.NotificationPatternRepository;
import com.ptit.hirex.dto.request.*;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.JobService;
import com.ptit.hirex.service.NotificationService;
import com.ptit.hirex.service.UserInfoService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/cms")
public class CmsController {
    private final JobService jobService;
    private final NotificationPatternRepository notificationPatternRepository;
    private final UserInfoService userInfoService;
    private final NotificationService notificationService;

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

    @GetMapping("/pattern")
    public ResponseEntity<List<NotificationPattern>> getAllPatterns() {
        return ResponseEntity.ok(notificationPatternRepository.getAll());
    }

    @PostMapping("/pattern")
    public ResponseEntity<Object> savePattern(@RequestBody NotificationPatternRequest pattern) {
        try{
            NotificationPattern notificationPattern = NotificationPattern.builder()
                    .subject(pattern.getSubject())
                    .patternCms(pattern.getPatternCms())
                    .type(pattern.getType())
                    .content(pattern.getContent())
                    .build();
            return ResponseEntity.ok(notificationPatternRepository.save(notificationPattern));
        }catch (Exception e){
            return null;
        }
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationCmsRequest request) throws MessagingException {
        notificationService.sendNotification(request);
        return ResponseEntity.ok().build();
    }
}
