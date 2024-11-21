package com.ptit.hirex.service;

import com.ptit.data.entity.*;
import com.ptit.data.repository.*;
import com.ptit.hirex.dto.FullJobDto;
import com.ptit.hirex.dto.UserInfoDto;
import com.ptit.hirex.dto.request.JobRequest;
import com.ptit.hirex.dto.request.JobSearchRequest;
import com.ptit.hirex.dto.response.EmployerResponse;
import com.ptit.hirex.dto.response.JobDTO;
import com.ptit.hirex.dto.response.JobResponse;
import com.ptit.hirex.dto.response.JobWithCompanyResponse;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final EmployeeSkillRepository employeeSkillRepository;
    private final JobSkillRepository jobSkillRepository;

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
            Job job = modelMapper.map(jobRequest, Job.class);
            job.setEmployer(employer.getId());

            jobRepository.save(job);

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
                    .email(job.getEmail())
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

            // Lấy tất cả job từ database
            List<Job> jobEntities = jobRepository.findAll();

            System.out.println("size: " + jobEntities.size());

            List<JobWithCompanyResponse> jobs = jobEntities.stream()
                    .map(job -> {
                        // Lấy thông tin district
                        String districtName = districtRepository.findById(job.getDistrictId())
                                .map(District::getName)
                                .orElse("");

                        // Lấy thông tin city
                        String cityName = cityRepository.findById(job.getCityId())
                                .map(City::getName)
                                .orElse("");

                        // Lấy thông tin employer và company
                        Employer employer = employerRepository.findById(job.getEmployer())
                                .orElse(null);

                        Company company = null;
                        if (employer != null) {
                            company = companyRepository.findById(employer.getCompany())
                                    .orElse(null);
                        }

                        return JobWithCompanyResponse.builder()
                                .id(job.getId())
                                .title(job.getTitle())
                                .location(job.getLocation())
                                .district(districtName)
                                .city(cityName)
                                .deadline(job.getDeadline())
                                .createdAt(job.getCreatedAt())
                                .companyName(company != null ? company.getCompanyName() : null)
                                .companyLogo(company != null ? company.getLogo() : null)
                                .companyDescription(company != null ? company.getDescription() : null)
                                .build();
                    })
                    .collect(Collectors.toList());

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
    public ResponseEntity<ResponseDto<Object>> searchJobs(JobSearchRequest searchRequest) {
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

        if (searchRequest.getSalaryOptions() != null && !searchRequest.getSalaryOptions().isEmpty()) {
            List<Predicate> salaryPredicates = searchRequest.getSalaryOptions().stream()
                    .map(salaryOption -> {
                        Long optionMinSalary = salaryOption.getMinSalary() != null
                                ? salaryOption.getMinSalary()
                                : 0L;

                        Long optionMaxSalary = salaryOption.getMaxSalary() != null
                                ? salaryOption.getMaxSalary()
                                : Long.MAX_VALUE;

                        // Only check if job's min salary is within the option's salary range
                        return criteriaBuilder.and(
                                // Job's min salary is greater than or equal to option's min salary
                                criteriaBuilder.greaterThanOrEqualTo(job.get("minSalary"), optionMinSalary),
                                // Job's min salary is less than or equal to option's max salary
                                criteriaBuilder.lessThanOrEqualTo(job.get("minSalary"), optionMaxSalary)
                        );
                    })
                    .toList();
            if (!salaryPredicates.isEmpty()) {
                predicates.add(criteriaBuilder.or(salaryPredicates.toArray(new Predicate[0])));
            }
        }
        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        List<Job> jobEntities = entityManager.createQuery(query).getResultList();

        List<JobWithCompanyResponse> jobs = jobEntities.stream()
                .map(jobItem -> {
                    // Lấy thông tin district
                    String districtName = districtRepository.findById(jobItem.getDistrictId())
                            .map(District::getName)
                            .orElse("");

                    // Lấy thông tin city
                    String cityName = cityRepository.findById(jobItem.getCityId())
                            .map(City::getName)
                            .orElse("");

                    // Lấy thông tin employer và company
                    Employer employer = employerRepository.findById(jobItem.getEmployer())
                            .orElse(null);

                    Company company = null;
                    if (employer != null) {
                        company = companyRepository.findById(employer.getCompany())
                                .orElse(null);
                    }

                    return JobWithCompanyResponse.builder()
                            .id(jobItem.getId())
                            .title(jobItem.getTitle())
                            .location(jobItem.getLocation())
                            .district(districtName)
                            .city(cityName)
                            .deadline(jobItem.getDeadline())
                            .createdAt(jobItem.getCreatedAt())
                            .companyName(company != null ? company.getCompanyName() : null)
                            .companyLogo(company != null ? company.getLogo() : null)
                            .companyDescription(company != null ? company.getDescription() : null)
                            .minSalary(jobItem.getMinSalary())
                            .maxSalary(jobItem.getMaxSalary())
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseBuilder.okResponse(
                languageService.getMessage("get.all.jobs.success"),
                jobs,
                StatusCodeEnum.JOB1001
        );
    }

    public List<FullJobDto> getFullDataJobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().map(job -> FullJobDto.builder()
                .id(job.getId())
                .industry(job.getIndustryId() != null
                        ? industryRepository.findById(job.getIndustryId()).orElse(null)
                        : null)
                .jobType(job.getJobTypeId() != null
                        ? jobTypeRepository.findById(job.getJobTypeId()).orElse(null)
                        : null)
                .district(job.getDistrictId() != null
                        ? districtRepository.findById(job.getDistrictId()).orElse(null)
                        : null)
                .city(job.getCityId() != null
                        ? cityRepository.findById(job.getCityId()).orElse(null)
                        : null)
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .educationLevel(job.getEducationLevelId() != null
                        ? educationLevelRepository.findById(job.getEducationLevelId()).orElse(null)
                        : null)
                .position(job.getPositionId() != null
                        ? positionRepository.findById(job.getPositionId()).orElse(null)
                        : null)
                .yearExperience(job.getYearExperience())
                .contractType(job.getContractTypeId() != null
                        ? contractTypeRepository.findById(job.getContractTypeId()).orElse(null)
                        : null)
                .skill_ids(job.getId() != null
                        ? jobSkillRepository.findByJobId(job.getId()).stream().map(JobSkill::getSkillId).toList()
                        : null)
                .build()).toList();
    }

}