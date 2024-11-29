package com.ptit.hirex.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerRequest {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String fullName;
    private String gender;
    private String phone;
    private Long companyId;
    private String company;
    private Long city;
    private Long district;
}
