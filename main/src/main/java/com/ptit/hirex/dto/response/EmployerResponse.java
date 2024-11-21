package com.ptit.hirex.dto.response;

import com.ptit.data.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployerResponse {
    private Long id;

    private Long userId;

    private Company company;

    private String fullName;

    private String email;

    private String gender;

    private String phoneNumber;

    private String address;

    private String avatar;
}
