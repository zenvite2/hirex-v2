package com.ptit.hirex.dto.request;

import com.ptit.data.dto.FullJobDto;
import com.ptit.hirex.dto.FullEmployeeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
@Builder
public class RecommendRequestDto {
    private List<FullJobDto> jobs;
    private FullEmployeeDto employee;
}