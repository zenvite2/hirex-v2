package com.ptit.hirex.service;

import com.ptit.data.entity.*;
import com.ptit.data.repository.*;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationPatternRepository patternRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final LanguageService languageService;
    private final EmployeeRepository employeeRepository;
    private final EmployerRepository employerRepository;

    public void createNotification(Long userId, Long jobId, String type) {
        Optional<NotificationPattern> patternOpt = patternRepository.findByType(type);
        if (patternOpt.isPresent()) {
            NotificationPattern pattern = patternOpt.get();

            User user = null;
            Job job = jobRepository.findById(jobId).orElse(null);
            String content = "";
            if (type.equals("APPLY")) {
                Employee employee = employeeRepository.findById(userId).orElse(null);
                user = userRepository.findById(employee.getUserId()).orElse(null);
                Optional<Employer> employer = employerRepository.findById(job.getEmployer());
                userId = employer.get().getUserId();

                content = pattern.getContent()
                        .replace("{user}", user.getFullName())
                        .replace("{job}", job.getTitle());
            } else {
                content = pattern.getContent()
                        .replace("{job}", job.getTitle());
            }

            Notification notification = new Notification();
            notification.setToUserId(userId);
            notification.setTitle(pattern.getSubject());
            notification.setContent(content);

            notificationRepository.save(notification);
        }
    }

    public ResponseEntity<ResponseDto<Object>> markAllNotifications(Long userId) {
        try {
            List<Notification> notifications = notificationRepository.findAllByToUserId(userId, Sort.by(Sort.Order.desc("createdAt")));

            notifications.forEach(notification -> notification.setRead(true));
            notificationRepository.saveAll(notifications);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("mark.notifications.read.success"),
                    StatusCodeEnum.NOTIFICATION1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("mark.notifications.read.failed"),
                    StatusCodeEnum.NOTIFICATION0000
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getNotification(Long userId) {

        try {
            List<Notification> list = notificationRepository.findAllByToUserId(userId, Sort.by(Sort.Order.desc("createdAt")));

            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.notification.success"),
                    list,
                    StatusCodeEnum.NOTIFICATION1001
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.notification.failed"),
                    StatusCodeEnum.NOTIFICATION0001
            );
        }
    }
}
