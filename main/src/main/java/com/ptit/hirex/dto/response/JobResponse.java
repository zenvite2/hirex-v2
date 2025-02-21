package com.ptit.hirex.dto.response;

import com.ptit.data.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponse {
    private Long id;
    private String title;
    private String location;
    private String district;
    private String city;
    private String deadline;
    private String description;
    private String requirement;
    private String yearExperience;
    private String benefit;
    private String workingTime;
    private Long minSalary;
    private Long maxSalary;
    private String position;
    private String jobType;
    private String contractType;
    private String email;
    private Company company;
    private EmployerResponse employer;
    private LocalDateTime createdAt;
    private Boolean active;
    private Integer countApplication;
}
