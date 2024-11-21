package com.ptit.hirex.dto;

import com.ptit.data.entity.CareerGoal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullEmployeeDto {
    private List<Long> educationLevelIds;
    private CareerGoal careerGoal;
    private List<Long> skillIds;
}
