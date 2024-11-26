package com.ptit.hirex.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowCompanyRequest {
    private Long companyId;
    private Long employeeId;
}
