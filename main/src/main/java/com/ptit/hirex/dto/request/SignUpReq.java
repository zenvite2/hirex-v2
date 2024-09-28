package com.ptit.hirex.dto.request;

import com.ptit.data.base.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReq {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotEmpty
    private List<Role> authorities;
}