package com.ptit.hirex.controller;

import com.ptit.hirex.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/recommend/{id}")
    @Cacheable(
            value = "recommendJobsCache",
            key = "'recommend_' + #id",
            condition = "#result != null && !#result.isEmpty()"
    )
    public Mono<List<?>> getRecommendJobs(@PathVariable Long id) {
        return recommendService.getListJobForRecommend(id);
    }

    @GetMapping("/similar/{id}")
    @Cacheable(
            value = "similarJobsCache",
            key = "'similar_' + #id",
            condition = "#result != null && !#result.isEmpty()"
    )
    public Mono<List<?>> getSimilarJobs(@PathVariable Long id) {
        return recommendService.getListJobForSimilar(id);
    }
}
