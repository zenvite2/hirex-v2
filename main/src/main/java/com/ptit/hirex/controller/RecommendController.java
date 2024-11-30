package com.ptit.hirex.controller;

import com.ptit.data.dto.FullJobDto;
import com.ptit.data.dto.JobWithCompanyResponse;
import com.ptit.data.entity.Notification;
import com.ptit.data.repository.JobRepository;
import com.ptit.hirex.service.JobService;
import com.ptit.hirex.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;
    private final JobService jobService;
    private final JobRepository jobRepository;
    private final WebClient wsWebClient;

    @GetMapping("/recommend/{id}")
    public Mono<List<?>> getRecommendJobs(@PathVariable Long id) {
        return recommendService.getListJobForRecommend(id);
    }

    @GetMapping("/similar/{id}")
    public Mono<List<?>> getSimilarJobs(@PathVariable Long id) {
        return recommendService.getListJobForSimilar(id);
    }

    @GetMapping("/test")
    public Mono<Void> test() {
        Notification notification = new Notification();

        return wsWebClient.post()
                .uri("/send-notification")
                .body(Mono.just(notification), Notification.class)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> log.info("Notification sent successfully"))
                .doOnError(e -> log.error("Error sending notification", e));
    }
}
