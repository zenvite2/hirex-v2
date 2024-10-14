package com.ptit.hirex.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerRequest {
    private String username;
    private String email;
    private String password;
    private String retryPassword;
    private String firstName;
    private String gender;
    private String phoneNumber;
    private String nameCompany;
    private Long city;
    private Long district;
}
