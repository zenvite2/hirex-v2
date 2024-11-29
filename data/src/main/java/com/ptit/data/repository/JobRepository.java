package com.ptit.data.repository;

import com.ptit.data.dto.FullJobDto;
import com.ptit.data.dto.JobWithCompanyResponse;
import com.ptit.data.dto.UserInfoDto;
import com.ptit.data.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByEmployer(Long employerId);

    @Query(nativeQuery = true, value = """
            SELECT 
                j.id AS jobId,
                j.title AS jobTitle,
                j.location AS jobLocation,
                d.name AS districtName,
                c.name AS cityName,
                j.deadline AS jobDeadline,
                j.created_at AS jobCreatedAt,
                j.contract_type_id AS contractTypeId,
                ct.name AS contractTypeName,
                j.min_salary AS minSalary,
                j.max_salary AS maxSalary,
                comp.company_name AS companyName,
                comp.logo AS companyLogo,
                comp.description AS companyDescription,
                e.user_id AS employerId,
                u.full_name AS employerName,
                u.email AS employerEmail,
                u.phone_number AS employerPhone,
                u.avatar AS employerAvatar
            FROM 
                jobs j
            LEFT JOIN district d ON j.district_id = d.id
            LEFT JOIN city c ON j.city_id = c.id
            LEFT JOIN contract_type ct ON j.contract_type_id = ct.id
            LEFT JOIN employer e ON j.employer = e.id
            LEFT JOIN company comp ON e.company_id = comp.id
            LEFT JOIN "user" u ON e.user_id = u.id
            """)
    List<Object[]> findAllJobsWithCompanyDetails();

    default List<JobWithCompanyResponse> getAllJobsWithCompany() {
        List<Object[]> results = findAllJobsWithCompanyDetails();

        return results.stream().map(result -> {
            UserInfoDto employerDto = new UserInfoDto();
            employerDto.setUserId(result[14] != null ? Long.valueOf(String.valueOf(result[14])) : null);
            employerDto.setFullName(result[15] != null ? String.valueOf(result[15]) : null);
            employerDto.setEmail(result[16] != null ? String.valueOf(result[16]) : null);
            employerDto.setPhoneNumber(result[17] != null ? String.valueOf(result[17]) : null);
            employerDto.setAvatar(result[18] != null ? String.valueOf(result[18]) : null);

            return JobWithCompanyResponse.builder()
                    .id(result[0] != null ? Long.valueOf(String.valueOf(result[0])) : null)
                    .title(result[1] != null ? String.valueOf(result[1]) : null)
                    .location(result[2] != null ? String.valueOf(result[2]) : null)
                    .district(result[3] != null ? String.valueOf(result[3]) : null)
                    .city(result[4] != null ? String.valueOf(result[4]) : null)
                    .deadline(result[5] != null ? String.valueOf(result[5]) : null)
                    .createdAt(result[6] instanceof LocalDateTime ? (LocalDateTime) result[6] : null)
                    .contractType(result[8] != null ? String.valueOf(result[8]) : null)
                    .minSalary(result[9] != null ? Long.valueOf(String.valueOf(result[9])) : null)
                    .maxSalary(result[10] != null ? Long.valueOf(String.valueOf(result[10])) : null)
                    .companyName(result[11] != null ? String.valueOf(result[11]) : null)
                    .companyLogo(result[12] != null ? String.valueOf(result[12]) : null)
                    .companyDescription(result[13] != null ? String.valueOf(result[13]) : null)
                    .employer(employerDto)
                    .build();
        }).collect(Collectors.toList());
    }

    @Query(nativeQuery = true, value = """
    SELECT 
        j.id AS jobId,
        j.title AS jobTitle,
        j.description AS jobDescription,
        j.benefit AS jobBenefit,
        j.requirement AS jobRequirement,
        j.location AS jobLocation,
        j.min_salary AS minSalary,
        j.max_salary AS maxSalary,
        j.year_experience AS yearExperience,
        j.deadline AS jobDeadline,
        
        city.id AS cityId,
        city.name AS cityName,
        
        district.id AS districtId,
        district.name AS districtName,
        
        position.id AS positionId,
        position.name AS positionName,
        
        job_type.id AS jobTypeId,
        job_type.name AS jobTypeName,
        
        contract_type.id AS contractTypeId,
        contract_type.name AS contractTypeName,
        
        industry.id AS industryId,
        industry.name AS industryName,
        
        education_level.id AS educationLevelId,
        education_level.name AS educationLevelName,
        
        (SELECT array_agg(skill_id) 
         FROM job_skill js 
         WHERE js.job_id = j.id) AS skillIds
    FROM 
        jobs j
    LEFT JOIN city ON j.city_id = city.id
    LEFT JOIN district ON j.district_id = district.id
    LEFT JOIN position ON j.position_id = position.id
    LEFT JOIN job_type ON j.job_type_id = job_type.id
    LEFT JOIN contract_type ON j.contract_type_id = contract_type.id
    LEFT JOIN industry ON j.industry_id = industry.id
    LEFT JOIN education_level ON j.education_level_id = education_level.id
    """)
    List<Object[]> findAllFullJobDetails();

    default List<FullJobDto> getFullDataJobs() {
        List<Object[]> results = findAllFullJobDetails();

        return results.stream().map(result -> {
            return FullJobDto.builder()
                    .id(getLongValue(result[0]))
                    .title(getStringValue(result[1]))
                    .description(getStringValue(result[2]))
                    .benefit(getStringValue(result[3]))
                    .requirement(getStringValue(result[4]))
                    .location(getStringValue(result[5]))
                    .minSalary(getLongValue(result[6]))
                    .maxSalary(getLongValue(result[7]))
                    .yearExperience(getLongValue(result[8]))
                    .deadline(getStringValue(result[9]))

                    .city(createSimpleEntity(City.class,
                            getLongValue(result[10]),
                            getStringValue(result[11])))

                    .district(createSimpleEntity(District.class,
                            getLongValue(result[12]),
                            getStringValue(result[13])))

                    .position(createSimpleEntity(Position.class,
                            getLongValue(result[14]),
                            getStringValue(result[15])))

                    .jobType(createSimpleEntity(JobType.class,
                            getLongValue(result[16]),
                            getStringValue(result[17])))

                    .contractType(createSimpleEntity(ContractType.class,
                            getLongValue(result[18]),
                            getStringValue(result[19])))

                    .industry(createSimpleEntity(Industry.class,
                            getLongValue(result[20]),
                            getStringValue(result[21])))

                    .educationLevel(createSimpleEntity(EducationLevel.class,
                            getLongValue(result[22]),
                            getStringValue(result[23])))

                    .skill_ids(result[24] != null
                            ? Arrays.stream((Long[]) result[24]).collect(Collectors.toList())
                            : Collections.emptyList())
                    .build();
        }).collect(Collectors.toList());
    }

    // Utility method for creating simple entities
    default <T> T createSimpleEntity(Class<T> entityClass, Long id, String name) {
        if (id == null) return null;
        try {
            Constructor<T> constructor = entityClass.getConstructor(Long.class, String.class);
            return constructor.newInstance(id, name);
        } catch (Exception e) {
            return null;
        }
    }

    // Utility method to safely get Long value
    default Long getLongValue(Object value) {
        if (value == null) return null;
        return value instanceof Long
                ? (Long) value
                : Long.valueOf(String.valueOf(value));
    }

    // Utility method to safely get String value
    default String getStringValue(Object value) {
        return value != null ? String.valueOf(value) : null;
    }
}
