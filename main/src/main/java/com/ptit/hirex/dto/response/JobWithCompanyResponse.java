package com.ptit.hirex.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobWithCompanyResponse {
    private Long id;
    private String title;
    private String location;
    private String district;
    private String city;
    private String deadline;
    private LocalDateTime createdAt;
    private String companyName;
    private String companyLogo;
    private String companyDescription;
}