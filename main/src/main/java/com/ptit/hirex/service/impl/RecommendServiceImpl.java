package com.ptit.hirex.service.impl;

import com.ptit.data.dto.FullJobDto;
import com.ptit.data.dto.JobWithCompanyResponse;
import com.ptit.data.repository.EmployeeRepository;
import com.ptit.data.repository.JobRepository;
import com.ptit.hirex.dto.FullEmployeeDto;
import com.ptit.hirex.dto.RecommendJobDto;
import com.ptit.hirex.dto.request.RecommendRequestDto;
import com.ptit.hirex.dto.request.SimilarRequestDto;
import com.ptit.hirex.service.EmployeeService;
import com.ptit.hirex.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
    private final WebClient webClient;
    private final ModelMapper modelMapper;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;

    public Mono<List<?>> getListJobForRecommend(Long userId) {
        Long employeeId = employeeRepository.findByUserId(userId).getId();
        List<FullJobDto> lstJobs = jobRepository.getFullDataJobs();
        FullEmployeeDto employeeDto = employeeService.getFullEmployeeData(employeeId);

        RecommendRequestDto requestDto = RecommendRequestDto.builder()
                .jobs(lstJobs)
                .employee(employeeDto)
                .build();

        return webClient.post()
                .uri("/recommend")
                .body(Mono.just(requestDto), RecommendRequestDto.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<RecommendJobDto>>() {
                })
                .timeout(Duration.ofSeconds(5))
                .flatMap(recommendJobDtos -> {
                    List<Long> jobIds = recommendJobDtos.stream()
                            .sorted(Comparator.comparing(RecommendJobDto::getSimilarityScore).reversed())
                            .map(RecommendJobDto::getJobId)
                            .toList();

                    if (jobIds.isEmpty()) {
                        return Mono.just(Collections.emptyList());
                    }

                    return Mono.just(
                            jobIds.stream()
                                    .map(jobId -> lstJobs.stream()
                                            .filter(job -> job.getId().equals(jobId))
                                            .findFirst()
                                            .orElse(null)
                                    )
                                    .filter(Objects::nonNull)
                                    .map(jobDto ->
                                            JobWithCompanyResponse.builder()
                                                    .id(jobDto.getId())
                                                    .companyLogo(jobDto.getCompanyLogo())
                                                    .companyName(jobDto.getCompanyName())
                                                    .companyDescription(jobDto.getCompanyDescription())
                                                    .title(jobDto.getTitle())
                                                    .jobType(jobDto.getJobType() != null ? jobDto.getJobType().getName() : null)
                                                    .contractType(jobDto.getContractType() != null ? jobDto.getContractType().getName() : null)
                                                    .city(jobDto.getCity() != null ? jobDto.getCity().getName() : null)
                                                    .district(jobDto.getDistrict() != null ? jobDto.getDistrict().getName() : null)
                                                    .minSalary(jobDto.getMinSalary() != null ? jobDto.getMinSalary() : null)
                                                    .maxSalary(jobDto.getMaxSalary() != null ? jobDto.getMaxSalary() : null)
                                                    .description(jobDto.getDescription() != null ? jobDto.getDescription() : null)
                                                    .build()
                                    )
                                    .collect(Collectors.toList())
                    );
                })
                .onErrorResume(this::handleError);
    }

    public Mono<List<?>> getListJobForSimilar(Long jobId) {
        List<FullJobDto> lstJobs = jobRepository.getFullDataJobs();
        SimilarRequestDto requestDto = SimilarRequestDto.builder()
                .jobs(lstJobs)
                .jobId(jobId)
                .build();

        return webClient.post()
                .uri("/similar")
                .body(Mono.just(requestDto), SimilarRequestDto.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<RecommendJobDto>>() {
                })
                .timeout(Duration.ofSeconds(5))
                .flatMap(recommendJobDtos -> {
                    // Create a map for efficient lookup
                    Map<Long, FullJobDto> jobMap = lstJobs.stream()
                            .collect(Collectors.toMap(FullJobDto::getId, Function.identity()));

                    List<Long> jobIds = recommendJobDtos.stream()
                            .sorted(Comparator.comparing(RecommendJobDto::getSimilarityScore).reversed())
                            .map(RecommendJobDto::getJobId)
                            .toList();

                    if (jobIds.isEmpty()) {
                        return Mono.just(Collections.emptyList());
                    }

                    return Mono.just(
                            jobIds.stream()
                                    .map(jobMap::get)
                                    .filter(Objects::nonNull)
                                    .map(jobDto ->
                                            JobWithCompanyResponse.builder()
                                                    .id(jobDto.getId())
                                                    .companyLogo(jobDto.getCompanyLogo())
                                                    .companyName(jobDto.getCompanyName())
                                                    .companyDescription(jobDto.getCompanyDescription())
                                                    .title(jobDto.getTitle())
                                                    .jobType(jobDto.getJobType() != null ? jobDto.getJobType().getName() : null)
                                                    .contractType(jobDto.getContractType() != null ? jobDto.getContractType().getName() : null)
                                                    .city(jobDto.getCity() != null ? jobDto.getCity().getName() : null)
                                                    .district(jobDto.getDistrict() != null ? jobDto.getDistrict().getName() : null)
                                                    .minSalary(jobDto.getMinSalary() != null ? jobDto.getMinSalary() : null)
                                                    .maxSalary(jobDto.getMaxSalary() != null ? jobDto.getMaxSalary() : null)
                                                    .description(jobDto.getDescription() != null ? jobDto.getDescription() : null)
                                                    .build()
                                    )
                                    .collect(Collectors.toList())
                    );
                })
                .onErrorResume(this::handleError);
    }

    private <T> Mono<T> handleError(Throwable error) {
        if (error instanceof WebClientResponseException responseError) {
            log.error("API Error: {}", responseError.getStatusCode());
        }
        return Mono.error(new RuntimeException("External API call failed", error));
    }

}
