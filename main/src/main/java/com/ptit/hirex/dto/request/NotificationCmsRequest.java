package com.ptit.hirex.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data
@Builder
public class NotificationCmsRequest {
    private String title;
    private String content;
    private Long sendTo;
}
