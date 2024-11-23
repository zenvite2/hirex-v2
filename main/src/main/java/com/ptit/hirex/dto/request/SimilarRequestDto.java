package com.ptit.hirex.dto.request;

import com.ptit.hirex.dto.FullJobDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
@Builder
public class SimilarRequestDto {
    private List<FullJobDto> jobs;
    private Long jobId;
}