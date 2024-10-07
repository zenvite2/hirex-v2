package com.ptit.hirex.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String gender;
}
