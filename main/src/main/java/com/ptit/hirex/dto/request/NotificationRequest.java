package com.ptit.hirex.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data
@Builder
public class NotificationRequest {
    private Long toUserId;

    private Long patternId;

    private HashMap<String, String> lstParams;
}
