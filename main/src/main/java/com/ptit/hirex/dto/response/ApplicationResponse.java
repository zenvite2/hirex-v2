package com.ptit.hirex.dto.response;

import com.ptit.data.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponse {
    private Long id;
    private Long jobId;
    private String jobTitle;  // từ bảng Job
    private String address;   // từ bảng Job
    private Long employeeId;
    private String fullName;  // từ bảng Employee
    private String coverLetter;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
}
