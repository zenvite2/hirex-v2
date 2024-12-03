package com.ptit.hirex.dto.response;

import com.ptit.data.converter.JSONListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeResponse {
    private Long id;

    private String title;

    private String career;

    private String hobby;

    private Long employeeId;

    private Boolean status;

    private List<Map<String, Object>> projects;

    private List<Map<String, Object>> certificates;

    private List<Map<String, Object>> educations;

    private List<Map<String, Object>> experiences;

    private List<Map<String, Object>> skills;

    private String fullName;

    private String email;

    private String dateOfBirth;

    private String phoneNumber;

    private String address;

    private String gender;

    private String avatar;
}
