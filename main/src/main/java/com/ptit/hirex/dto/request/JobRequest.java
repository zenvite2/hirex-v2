package com.ptit.hirex.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequest {
    private String title;
    private String description;
    private String location;
    private String requirement;
    private Long yearExperience;
    private Long minSalary;
    private Long maxSalary;
    private Long city;
    private Long district;
    private Long position;
    private Long jobType;
    private Long contractType;
    private String deadline;
    private Long education;
    private String benefit;
    private String workingTime;
    private String email;
    private String phone;
    private Long industry;
}