package com.ptit.hirex.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationPatternRequest {
    @NotBlank
    private String subject;

    @NotBlank
    private String content;

    private Boolean hasParams = false;
}
