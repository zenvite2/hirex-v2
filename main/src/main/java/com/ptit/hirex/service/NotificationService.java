package com.ptit.hirex.service;

import com.ptit.data.entity.Job;
import com.ptit.data.entity.Notification;
import com.ptit.data.entity.NotificationPattern;
import com.ptit.data.entity.User;
import com.ptit.data.repository.JobRepository;
import com.ptit.data.repository.NotificationPatternRepository;
import com.ptit.data.repository.NotificationRepository;
import com.ptit.data.repository.UserRepository;
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

    public void createNotification(Long userId, Long jobId, String type){
        Optional<NotificationPattern> patternOpt = patternRepository.findByType(type);
        if (patternOpt.isPresent()) {
            NotificationPattern pattern = patternOpt.get();
            User user = userRepository.findById(userId).orElse(null);
            Job job = jobRepository.findById(jobId).orElse(null);

            String content = pattern.getContent()
                    .replace("{user}", user.getFullName())
                    .replace("{job}", job.getTitle());

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

    public ResponseEntity<ResponseDto<Object>> getNotification(Long userId){

        try{
            List<Notification> list = notificationRepository.findAllByToUserId(userId, Sort.by(Sort.Order.desc("createdAt")));

            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.notification.success"),
                    list,
                    StatusCodeEnum.NOTIFICATION1001
            );
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.notification.failed"),
                    StatusCodeEnum.NOTIFICATION0001
            );
        }
    }
}
