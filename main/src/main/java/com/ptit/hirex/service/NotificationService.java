package com.ptit.hirex.service;

import com.ptit.data.entity.*;
import com.ptit.data.repository.*;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationPatternRepository patternRepository;
    private final NotificationRepository notificationRepository;
    private final JobRepository jobRepository;
    private final LanguageService languageService;
    private final WebClient wsWebClient;

    public void createNotification(Long userId, Long jobId, String type) {
        Optional<NotificationPattern> patternOpt = patternRepository.findByType(type);
        if (patternOpt.isPresent()) {
            NotificationPattern pattern = patternOpt.get();

            Job job = jobRepository.findById(jobId).orElse(null);
            String content = pattern.getContent()
                    .replace("{job}", job.getTitle());

            Notification notification = new Notification();
            notification.setToUserId(userId);
            notification.setTitle(pattern.getSubject());
            notification.setContent(content);
            Notification newNotification = notificationRepository.save(notification);

            try {
                wsWebClient.post()
                        .uri("/send-notification")
                        .body(Mono.just(newNotification), Notification.class)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block(); // Block and wait for completion
                log.info("Notification sent successfully");
            } catch (Exception e) {
                log.error("Error sending notification", e);
            }
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
