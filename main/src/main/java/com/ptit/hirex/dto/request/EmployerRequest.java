package com.ptit.hirex.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerRequest {
    private String nameCompany;
    private Long address;
    private String description;
    private String website;
    private String phoneNumber;
    private MultipartFile avatar;
}
