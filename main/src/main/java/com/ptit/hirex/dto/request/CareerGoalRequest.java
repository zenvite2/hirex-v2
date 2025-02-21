package com.ptit.hirex.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CareerGoalRequest {
    private Long industry;

    private Long position;

    private Long minSalary;

    private Long maxSalary;

    private Long jobType;
}
