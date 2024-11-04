package com.ptit.hirex.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;

    private Long userId;

    private String fullName;

    private String email;

    private String dateOfBirth;

    private String phoneNumber;

    private String address;

    private BigDecimal salary;

    private String gender;

    private String avatar;
}
