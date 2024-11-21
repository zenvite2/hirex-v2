package com.ptit.hirex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendJobDto {
    private Long jobId;
    private Double similarityScore;
}
