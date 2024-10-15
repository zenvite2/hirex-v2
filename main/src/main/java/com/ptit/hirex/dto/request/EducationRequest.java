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

    private String nameSchool;

    private String expertise;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;
}
