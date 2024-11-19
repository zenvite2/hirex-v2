package com.ptit.hirex.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDTO {
    private Long id;
    private String title;
    private String location;
    private Long district;
    private Long city;
    private String deadline;
    private String description;
    private String requirement;
    private String yearExperience;
    private String benefit;
    private String workingTime;
    private Long minSalary;
    private Long maxSalary;
    private Long position;
    private Long jobType;
    private Long contractType;
    private Long education;
    private Long industry;
    private String email;
    private LocalDateTime createdAt;
}
