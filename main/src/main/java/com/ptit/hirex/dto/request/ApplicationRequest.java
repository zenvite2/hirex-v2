package com.ptit.hirex.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationRequest {
    private Long jobId;
    private String coverLetter;
    private MultipartFile cvPdf;
    private Long resumeId;
}
