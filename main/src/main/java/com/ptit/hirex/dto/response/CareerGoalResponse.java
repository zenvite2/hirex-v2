package com.ptit.hirex.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CareerGoalResponse {
    private Long position;
    private Long maxSalary;
    private Long minSalary;
    private Long jobType;
}
