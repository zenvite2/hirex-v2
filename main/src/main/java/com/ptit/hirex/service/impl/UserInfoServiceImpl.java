package com.ptit.hirex.service.impl;

import com.ptit.data.entity.User;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.UserInfoDto;
import com.ptit.hirex.dto.request.UserStatusRequest;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.LanguageService;
import com.ptit.hirex.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public ResponseEntity<ResponseDto<Object>> getAllUser() {
        try{
            List<User> list = userRepository.findAll();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("user-info.success"),
                    list,
                    StatusCodeEnum.USERINFO1000
            );
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("user-info.success"),
                    StatusCodeEnum.USERINFO1000
            );
        }
    }

    @Override
    public ResponseEntity<ResponseDto<Object>> updateUser(Long userId, UserStatusRequest userStatusRequest) {
        try{
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseBuilder.noContentResponse(languageService.getMessage("user-info.not-found"), StatusCodeEnum.USERINFO0000);
            }

            user.setActive(userStatusRequest.getActive());

            userRepository.save(user);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.user-info.success"),
                    StatusCodeEnum.USERINFO1000
            );
        }catch (Exception e){
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("update.user-info.failed"),
                    StatusCodeEnum.USERINFO1000
            );
        }
    }
}
