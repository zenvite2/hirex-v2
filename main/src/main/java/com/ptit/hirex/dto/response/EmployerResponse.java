package com.ptit.hirex.dto.response;

import com.ptit.data.entity.Company;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
