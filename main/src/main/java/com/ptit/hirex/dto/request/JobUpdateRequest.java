package com.ptit.hirex.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobUpdateRequest {
    private String title;
    private String description;
    private String location;
    private String requirements;
    private Long yearExperience;
    private Long salary;
    private Long city;
    private Long district;
    private Long position;
    private Long jobType;
    private Long contractType;
    private String deadline;
}
