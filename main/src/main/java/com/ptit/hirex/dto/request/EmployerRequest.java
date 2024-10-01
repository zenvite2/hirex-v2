package com.ptit.hirex.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerRequest {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String gender;
    private String nameCompany;
    private Integer address;
}
