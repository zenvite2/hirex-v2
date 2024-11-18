package com.ptit.hirex.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedJobResponse {
    private Long id;
    private JobResponse jobResponse;
    private Long employeeId;
    private LocalDateTime createdAt;
}
