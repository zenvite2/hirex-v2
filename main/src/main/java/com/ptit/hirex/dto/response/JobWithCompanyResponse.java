package com.ptit.hirex.dto.response;

import com.ptit.hirex.dto.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

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
    //    private String salary;
    private Long minSalary;
    private Long maxSalary;
    private UserInfoDto employer;
    private Map<String, Object> jobDetails;
}