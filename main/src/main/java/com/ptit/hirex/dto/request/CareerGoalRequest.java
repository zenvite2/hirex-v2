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
    private String position;

    private Long salary;

    private Long jobType;
}
