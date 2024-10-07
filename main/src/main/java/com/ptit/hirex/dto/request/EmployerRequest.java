package com.ptit.hirex.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerRequest {
    private String fullName;
    private String email;
    private String password;
    private String retryPassword;
    private String nameCompany;
    private Integer gender;
    private Long address;
    private String phoneNumber;
}
