package com.ptit.hirex.dto;

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
public class CompanyDTO {

    private Long id;

    private Long employerId;

    private String companyName;

    private String description;

    private String website;

    private String logoUrl;

    private String bannerUrl;

    private String address;

    private Long city;

    private Long district;

    private Long scale;

}
