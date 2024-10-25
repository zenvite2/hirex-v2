package com.ptit.hirex.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data
@Builder
public class NotificationRequest {
    @NotNull
    private Long toUserId;

    @NotNull
    private Long patternId;

    private HashMap<String, String> lstParams;
}
