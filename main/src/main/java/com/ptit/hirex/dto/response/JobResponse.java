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
public class JobResponse {
    private Long id;
    private String title;
    private String location;
    private Long district;
    private Long city;
    private String deadline;
    private String description;
    private String requirements;
    private Long yearExperience;
    private Long salary;
    private Long tech;
    private Long position;
    private Long jobType;
    private Long contractType;
    private LocalDateTime createdAt;
}
