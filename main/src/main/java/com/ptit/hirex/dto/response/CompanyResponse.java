package com.ptit.hirex.dto.response;

import com.ptit.data.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyResponse {
    private Long id;

    private Long employerId;

    private String companyName;

    private String description;

    private String website;

    private String logo;

    private String address;

    private String city;

    private String district;

    private Long scale;

    private List<Job> jobs;
}
