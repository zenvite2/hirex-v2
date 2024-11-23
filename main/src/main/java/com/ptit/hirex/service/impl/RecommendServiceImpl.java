package com.ptit.hirex.service.impl;

import com.ptit.data.entity.*;
import com.ptit.data.repository.*;
import com.ptit.hirex.dto.FullEmployeeDto;
import com.ptit.hirex.dto.FullJobDto;
import com.ptit.hirex.dto.RecommendJobDto;
import com.ptit.hirex.dto.request.RecommendRequestDto;
import com.ptit.hirex.dto.request.SimilarRequestDto;
import com.ptit.hirex.dto.response.JobWithCompanyResponse;
import com.ptit.hirex.service.CompanyService;
import com.ptit.hirex.service.EmployeeService;
import com.ptit.hirex.service.JobService;
import com.ptit.hirex.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
    private final WebClient webClient;
    private final JobService jobService;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final EmployerRepository employerRepository;
    private final DistrictRepository districtRepository;
    private final CityRepository cityRepository;
    private final CompanyRepository companyRepository;
    private final JobTypeRepository jobTypeRepository;

    public Mono<List<?>> getListJobForRecommend(Long userId) {
        Long employeeId = employeeRepository.findByUserId(userId).getId();
        List<FullJobDto> lstJobs = jobService.getFullDataJobs();
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
                            .collect(Collectors.toList());

                    return jobIds.isEmpty()
                            ? Mono.just(Collections.emptyList())
                            : Flux.fromIterable(jobIds)
                            .flatMap(this::createJobWithCompanyResponse)
                            .collectList();
                })
                .onErrorResume(this::handleError);
    }

    public Mono<List<?>> getListJobForSimilar(Long jobId) {
        List<FullJobDto> lstJobs = jobService.getFullDataJobs();
        SimilarRequestDto requestDto = SimilarRequestDto.builder()
                .jobs(lstJobs)
                .jobId(jobId)
                .build();

        return webClient.post()
                .uri("/similar")
                .body(Mono.just(requestDto), RecommendRequestDto.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<RecommendJobDto>>() {
                })
                .timeout(Duration.ofSeconds(5))
                .flatMap(recommendJobDtos -> {
                    List<Long> jobIds = recommendJobDtos.stream()
                            .sorted(Comparator.comparing(RecommendJobDto::getSimilarityScore).reversed())
                            .map(RecommendJobDto::getJobId)
                            .collect(Collectors.toList());

                    return jobIds.isEmpty()
                            ? Mono.just(Collections.emptyList())
                            : Flux.fromIterable(jobIds)
                            .flatMap(this::createJobWithCompanyResponse)
                            .collectList();
                })
                .onErrorResume(this::handleError);
    }

    private <T> Mono<T> handleError(Throwable error) {
        if (error instanceof WebClientResponseException responseError) {
            log.error("API Error: {}", responseError.getStatusCode());
        }
        return Mono.error(new RuntimeException("External API call failed", error));
    }

    private Mono<JobWithCompanyResponse> createJobWithCompanyResponse(Long jobId) {
        Optional<Job> jobOpt = jobRepository.findById(jobId);

        return jobOpt.map(job -> {
            String districtName = districtRepository.findById(job.getDistrictId())
                    .map(District::getName)
                    .orElse("");

            String jobTypeName = jobTypeRepository.findById(job.getJobTypeId())
                    .map(JobType::getName)
                    .orElse("");

            String cityName = cityRepository.findById(job.getCityId())
                    .map(City::getName)
                    .orElse("");

            Optional<Employer> employerOpt = employerRepository.findById(job.getEmployer());

            Optional<Company> companyOpt = employerOpt
                    .flatMap(employer -> companyRepository.findById(employer.getCompany()));

            return JobWithCompanyResponse.builder()
                    .id(job.getId())
                    .title(job.getTitle())
                    .location(job.getLocation())
                    .district(districtName)
                    .city(cityName)
                    .jobType(jobTypeName)
                    .deadline(job.getDeadline())
                    .createdAt(job.getCreatedAt())
                    .minSalary(job.getMinSalary())
                    .maxSalary(job.getMaxSalary())
                    .companyName(companyOpt.map(Company::getCompanyName).orElse(null))
                    .companyLogo(companyOpt.map(Company::getLogo).orElse(null))
                    .companyDescription(companyOpt.map(Company::getDescription).orElse(null))
                    .build();
        }).map(Mono::just).orElse(Mono.empty());
    }
}
