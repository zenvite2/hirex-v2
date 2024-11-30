package com.ptit.hirex.service;

import com.ptit.data.dto.FullJobDto;
import com.ptit.data.dto.JobWithCompanyResponse;
import com.ptit.data.entity.*;
import com.ptit.data.repository.*;
import com.ptit.hirex.dto.MetaData;
import com.ptit.hirex.dto.request.JobRequest;
import com.ptit.hirex.dto.request.JobSearchRequest;
import com.ptit.hirex.dto.response.EmployerResponse;
import com.ptit.hirex.dto.response.JobDTO;
import com.ptit.hirex.dto.response.JobResponse;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final LanguageService languageService;
    private final ModelMapper modelMapper;
    private final EmployerRepository employerRepository;
    private final DistrictRepository districtRepository;
    private final CityRepository cityRepository;
    private final YearExperienceRepository experienceRepository;
    private final PositionRepository positionRepository;
    private final JobTypeRepository jobTypeRepository;
    private final ContractTypeRepository contractTypeRepository;
    private final CompanyRepository companyRepository;
    private final EntityManager entityManager;
    private final IndustryRepository industryRepository;
    private final EducationLevelRepository educationLevelRepository;
    private final JobSkillRepository jobSkillRepository;
    private final FollowCompanyService followCompanyService;
    private final NotificationService notificationService;
    private final EmployeeRepository employeeRepository;
    private final MailService mailService;

    public ResponseEntity<ResponseDto<Object>> createJob(JobRequest jobRequest) {

        String userName = authenticationService.getUserFromContext();

        Optional<User> optionalUser = userRepository.findByUsername(userName);

        if (optionalUser.isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.signup.user.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        User user = optionalUser.get();

        Employer employer = employerRepository.findByUserId(user.getId());
        if (employer == null) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("employer.not.found"),
                    StatusCodeEnum.EMPLOYER4000
            );
        }

        try {
            Job job = new Job();
            modelMapper.map(jobRequest, job);
            job.setEmployer(employer.getId());

            Job savedJob = jobRepository.save(job);

            notificationService.createNotification(employer.getUserId(), job.getId(), "JOB_POSTED");

            if (jobRequest.getSkills() != null && !jobRequest.getSkills().isEmpty()) {
                List<JobSkill> jobSkills = jobRequest.getSkills().stream()
                        .map(skillId -> JobSkill.builder()
                                .jobId(savedJob.getId())
                                .skillId(skillId)
                                .build())
                        .collect(Collectors.toList());

                // Lưu các JobSkill vào database
                jobSkillRepository.saveAll(jobSkills);
            }

            List<FollowCompany> followCompany = followCompanyService.getListFollow();

            for(FollowCompany follow: followCompany){
                if(follow.getCompanyId() == employer.getCompany()){

                    Optional<User> user1 = userRepository.findById(follow.getEmployeeId());

                    notificationService.createNotification(follow.getEmployeeId(), job.getId(), "FOLLOW");
                    mailService.sendEmailFollow(user1.get().getEmail(), companyRepository.findById(employer.getCompany()).get().getCompanyName(), "https://deploy-hirexptit.io.vn/");
                }
            }

            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.job.success"),
                    job,
                    StatusCodeEnum.JOB1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.job.failed"),
                    StatusCodeEnum.JOB0000
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> updateJob(Long id, JobRequest jobRequest) {
        try {
            Job job = jobRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Not found job"));

            if (jobRequest != null) {
                modelMapper.map(jobRequest, job);
            }

            jobRepository.save(job);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.job.success"),
                    job,
                    StatusCodeEnum.JOB1002
            );
        } catch (NoSuchElementException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("not.found.job"),
                    StatusCodeEnum.JOB4000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("update.job.failed"),
                    StatusCodeEnum.JOB0002
            );
        }
    }


    public ResponseEntity<ResponseDto<Object>> getJob(Long id) {
        try {
            Job job = jobRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(languageService.getMessage("not.found.job")));

            JobDTO jobResponse = modelMapper.map(job, JobDTO.class);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.job.success"),
                    jobResponse,
                    StatusCodeEnum.JOB1001
            );
        } catch (NoSuchElementException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("not.found.job"),
                    StatusCodeEnum.JOB4000
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.job.failed"),
                    StatusCodeEnum.JOB0001
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getJobDetail(Long id) {
        try {
            Job job = jobRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(languageService.getMessage("not.found.job")));

            // Lấy thông tin employer và company
            Employer employer = employerRepository.findById(job.getEmployer())
                    .orElse(null);

            Company company = null;
            if (employer != null) {
                company = companyRepository.findById(employer.getCompany())
                        .orElse(null);
            }

            User user = userRepository.findById(employer.getUserId()).orElse(null);

            EmployerResponse employerResponse = EmployerResponse.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .avatar(user.getAvatar())
                    .fullName(user.getFullName())
                    .phoneNumber(user.getPhoneNumber())
                    .build();

            JobResponse jobResponse = JobResponse.builder()
                    .id(job.getId())
                    .title(job.getTitle())
                    .location(job.getLocation())
                    .district(districtRepository.findById(job.getDistrictId()).get().getName())
                    .city(cityRepository.findById(job.getCityId()).get().getName())
                    .deadline(job.getDeadline())
                    .description(job.getDescription())
                    .requirement(job.getRequirement())
                    .yearExperience(experienceRepository.findById(job.getYearExperience()).get().getName())
                    .minSalary(job.getMinSalary())
                    .maxSalary(job.getMaxSalary())
                    .benefit(job.getBenefit())
                    .workingTime(job.getWorkingTime())
                    .position(positionRepository.findById(job.getPositionId()).get().getName())
                    .jobType(jobTypeRepository.findById(job.getJobTypeId()).get().getName())
                    .contractType(contractTypeRepository.findById(job.getContractTypeId()).get().getName())
                    .createdAt(job.getCreatedAt())
                    .company(company)
                    .employer(employerResponse)
                    .build();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.job.success"),
                    jobResponse,
                    StatusCodeEnum.JOB1001
            );
        } catch (NoSuchElementException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("not.found.job"),
                    StatusCodeEnum.JOB4000
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.job.failed"),
                    StatusCodeEnum.JOB0001
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getAllJob() {
        try {
            String userName = authenticationService.getUserFromContext();

            Optional<User> optionalUser = userRepository.findByUsername(userName);

            if (optionalUser.isEmpty()) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.signup.user.not.found"),
                        StatusCodeEnum.AUTH0016
                );
            }

            User user = optionalUser.get();

            Employer employer = employerRepository.findByUserId(user.getId());
            if (employer == null) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("employer.not.found"),
                        StatusCodeEnum.EMPLOYER4000
                );
            }

            List<Job> jobEntities = jobRepository.findAllByEmployer(employer.getId());
            List<JobResponse> jobs = jobEntities.stream()
                    .map(job -> {
                        String districtName = districtRepository.findById(job.getDistrictId())
                                .map(District::getName)
                                .orElse("");

                        String cityName = cityRepository.findById(job.getCityId())
                                .map(City::getName)
                                .orElse("");

                        return JobResponse.builder()
                                .id(job.getId())
                                .title(job.getTitle())
                                .location(job.getLocation())
//                                .district(districtName)
//                                .city(cityName)
                                .minSalary(job.getMinSalary())
                                .maxSalary(job.getMaxSalary())
                                .deadline(job.getDeadline())
                                .createdAt(job.getCreatedAt())
                                .build();
                    })
                    .collect(Collectors.toList());

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.all.jobs.success"),
                    jobs,
                    StatusCodeEnum.JOB1001
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.all.jobs.failed"),
                    StatusCodeEnum.JOB0001
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getAllJobsWithCompany() {
        try {
            List<JobWithCompanyResponse> jobs = jobRepository.getAllJobsWithCompany();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.all.jobs.success"),
                    jobs,
                    StatusCodeEnum.JOB1001
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.all.jobs.failed"),
                    StatusCodeEnum.JOB0001
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> deleteJob(Long id) {
        try {
            Job job = jobRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(languageService.getMessage("not.found.job")));

            jobRepository.deleteById(id);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("delete.job.success"),
                    StatusCodeEnum.JOB1003
            );
        } catch (NoSuchElementException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("not.found.job"),
                    StatusCodeEnum.JOB4000
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("delete.job.failed"),
                    StatusCodeEnum.JOB0003
            );
        }
    }

    @Transactional
    public ResponseEntity<ResponseDto<Object>> searchJobs(JobSearchRequest searchRequest, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Job> query = criteriaBuilder.createQuery(Job.class);
        Root<Job> job = query.from(Job.class);

        List<Predicate> predicates = new ArrayList<>();

        // Apply filters based on input parameters
        if (searchRequest.getSearchQuery() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(job.get("title")), "%" + searchRequest.getSearchQuery().toLowerCase() + "%"));
        }
        if (searchRequest.getCity() != null) {
            predicates.add(criteriaBuilder.equal(job.get("cityId"), searchRequest.getCity()));
        }
        if (searchRequest.getExperienceIds() != null && !searchRequest.getExperienceIds().isEmpty()) {
            predicates.add(job.get("yearExperience").in(searchRequest.getExperienceIds()));
        }
        if (searchRequest.getIndustryIds() != null && !searchRequest.getIndustryIds().isEmpty()) {
            predicates.add(job.get("industryId").in(searchRequest.getIndustryIds()));
        }
        if (searchRequest.getJobTypeIds() != null && !searchRequest.getJobTypeIds().isEmpty()) {
            predicates.add(job.get("jobTypeId").in(searchRequest.getJobTypeIds()));
        }
        if (searchRequest.getPositionIds() != null && !searchRequest.getPositionIds().isEmpty()) {
            predicates.add(job.get("positionId").in(searchRequest.getPositionIds()));
        }
        if (searchRequest.getContractTypeIds() != null && !searchRequest.getContractTypeIds().isEmpty()) {
            predicates.add(job.get("contractTypeId").in(searchRequest.getContractTypeIds()));
        }
        if (searchRequest.getEducationIds() != null && !searchRequest.getEducationIds().isEmpty()) {
            predicates.add(job.get("educationLevelId").in(searchRequest.getEducationIds()));
        }
        
        predicates.add(criteriaBuilder.isTrue(job.get("active")));

        if (searchRequest.getSalaryOptions() != null && !searchRequest.getSalaryOptions().isEmpty()) {
            List<Predicate> salaryPredicates = searchRequest.getSalaryOptions().stream()
                    .map(salaryOption -> {
                        Long optionMinSalary = salaryOption.getMinSalary() != null
                                ? salaryOption.getMinSalary()
                                : 0L;

                        Long optionMaxSalary = salaryOption.getMaxSalary() != null
                                ? salaryOption.getMaxSalary()
                                : Long.MAX_VALUE;

                        return criteriaBuilder.and(
                                criteriaBuilder.greaterThanOrEqualTo(job.get("minSalary"), optionMinSalary),
                                criteriaBuilder.lessThanOrEqualTo(job.get("minSalary"), optionMaxSalary)
                        );
                    })
                    .toList();
            if (!salaryPredicates.isEmpty()) {
                predicates.add(criteriaBuilder.or(salaryPredicates.toArray(new Predicate[0])));
            }
        }

        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        // Sử dụng Pageable để lấy phân trang
        TypedQuery<Job> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Job> jobEntities = typedQuery.getResultList();

        // Create a separate query to get total items
        TypedQuery<Job> totalQuery = entityManager.createQuery(query);
        List<Job> totalJobEntities = totalQuery.getResultList();

        int totalItems = totalJobEntities.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageable.getPageSize());

        MetaData metaData = new MetaData();
        metaData.setTotalPages(totalPages);
        metaData.setCurrentPage(pageable.getPageNumber());
        metaData.setPageSize(pageable.getPageSize());
        metaData.setTotalItems(totalItems);

        List<JobWithCompanyResponse> jobs = jobEntities.stream()
                .map(jobItem -> {
                    // Mapping job to JobWithCompanyResponse
                    String districtName = districtRepository.findById(jobItem.getDistrictId())
                            .map(District::getName)
                            .orElse("");
                    String cityName = cityRepository.findById(jobItem.getCityId())
                            .map(City::getName)
                            .orElse("");
                    String contractTypeName = contractTypeRepository.findById(jobItem.getContractTypeId())
                            .map(ContractType::getName)
                            .orElse("");
                    Employer employer = employerRepository.findById(jobItem.getEmployer()).orElse(null);
                    Company company = employer != null ? companyRepository.findById(employer.getCompany()).orElse(null) : null;

                    return JobWithCompanyResponse.builder()
                            .id(jobItem.getId())
                            .title(jobItem.getTitle())
                            .location(jobItem.getLocation())
                            .district(districtName)
                            .city(cityName)
                            .contractType(contractTypeName)
                            .deadline(jobItem.getDeadline())
                            .createdAt(jobItem.getCreatedAt())
                            .companyName(company != null ? company.getCompanyName() : null)
                            .companyLogo(company != null ? company.getLogo() : null)
                            .companyDescription(company != null ? company.getDescription() : null)
                            .description(jobItem.getDescription())
                            .minSalary(jobItem.getMinSalary())
                            .maxSalary(jobItem.getMaxSalary())
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseBuilder.okResponse(
                languageService.getMessage("get.all.jobs.success"),
                jobs,
                StatusCodeEnum.JOB1001,
                metaData
        );
    }

}