package com.ptit.hirex.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceRequest {
    private String companyName;

    private Long position;

    private String startDate;

    private String endDate;

    private String description;

    private Long yearExperience;
}
