package com.ptit.hirex.service;

import com.ptit.hirex.dto.UserInfoDto;
import com.ptit.hirex.model.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserInfoService {

    ResponseEntity<ResponseDto<UserInfoDto>> getUserInfoById(Long userId);
}
