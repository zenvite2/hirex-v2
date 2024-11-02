package com.ptit.hirex.service.impl;

import com.ptit.data.entity.User;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.UserInfoDto;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.LanguageService;
import com.ptit.hirex.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;

    private final LanguageService languageService;

    public ResponseEntity<ResponseDto<UserInfoDto>> getUserInfoById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseBuilder.noContentResponse(languageService.getMessage("user-info.not-found"), StatusCodeEnum.USERINFO0000);
        }
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .fullName(user.getFullName())
                .userId(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .build();
        return ResponseBuilder.okResponse(languageService.getMessage("user-info.success"), userInfoDto, StatusCodeEnum.USERINFO1000);
    }
}
