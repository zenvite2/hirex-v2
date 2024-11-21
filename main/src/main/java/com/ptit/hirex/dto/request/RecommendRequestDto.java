package com.ptit.hirex.dto.request;

import com.ptit.hirex.dto.FullEmployeeDto;
import com.ptit.hirex.dto.FullJobDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class RecommendRequestDto {
    private List<FullJobDto> jobs;
    private FullEmployeeDto employee;
}