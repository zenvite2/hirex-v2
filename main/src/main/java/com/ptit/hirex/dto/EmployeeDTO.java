package com.ptit.hirex.dto;

import com.ptit.hirex.annotation.IsValidImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private String email;
    private String fullName;
    private String phoneNumber;
    private String gender;
    private String dateOfBirth;
    private String address;
    @IsValidImage(maxSize = 10 * 1024 * 1024)
    private MultipartFile avatar;
}