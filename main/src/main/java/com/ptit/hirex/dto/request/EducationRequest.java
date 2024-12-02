package com.ptit.hirex.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationRequest {
    private String level;

    private  Long educationLevelId;

    private String universityName;

    private String expertise;

    private String startDate;

    private String endDate;

    private String description;
}
