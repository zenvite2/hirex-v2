package com.ptit.security.service;


import com.ptit.data.base.User;
import com.ptit.security.dto.UserDto;
import com.ptit.security.dto.request.SignInReq;
import com.ptit.security.dto.response.SignInRes;

public interface UserService {

//    User createUser(UserDto userDTO) throws Exception;

//    User updatePassword(String phoneNumber, String oldPassword, String newPassword) throws Exception;

    SignInRes login(SignInReq signInReq) throws Exception;

//    User findByPhoneNumber(String phoneNumber);

    User getUserFromContext();

}
