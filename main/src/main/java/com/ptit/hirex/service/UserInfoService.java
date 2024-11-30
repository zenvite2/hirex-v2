package com.ptit.hirex.service;

import com.ptit.data.entity.User;
import com.ptit.hirex.dto.UserInfoDto;
import com.ptit.hirex.dto.request.UserStatusRequest;
import com.ptit.hirex.model.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserInfoService {
    ResponseEntity<ResponseDto<UserInfoDto>> getUserInfoById(Long userId);

    ResponseEntity<ResponseDto<Object>> getAllUser();

    ResponseEntity<ResponseDto<Object>> updateUser(Long userId, UserStatusRequest userStatusRequest);
}
