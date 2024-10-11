package com.ptit.hirex.dto.request;

import com.ptit.hirex.annotation.IsValidImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformationRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private Integer gender;
    private LocalDateTime dateOfBirth;
    private Integer address;
    @IsValidImage(maxSize = 10 * 1024 * 1024)
    private MultipartFile avatar;
}
