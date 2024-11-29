package com.ptit.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobWithCompanyResponse {
    private Long id;
    private String title;
    private String location;
    private String district;
    private String city;
    private String deadline;
    private LocalDateTime createdAt;
    private String jobType;
    private String contractType;
    private String companyName;
    private String companyLogo;
    private String companyDescription;
    private Long minSalary;
    private Long maxSalary;
    private UserInfoDto employer;
}