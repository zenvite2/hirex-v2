package com.ptit.hirex.service;

import reactor.core.publisher.Mono;

import java.util.List;

public interface RecommendService {

    Mono<List<?>> getListJobForRecommend(Long employeeId);
}
