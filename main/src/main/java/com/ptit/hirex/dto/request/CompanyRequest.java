package com.ptit.hirex.dto.request;

import com.ptit.hirex.annotation.IsValidImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyRequest {
    private String companyName;

    private String description;

    private String website;

    @IsValidImage(maxSize = 10 * 1024 * 1024)
    private MultipartFile logo;

    private String address;

    private Long city;

    private Long district;

    private Long scale;
}
