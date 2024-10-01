package com.ptit.hirex.security.service;


import com.ptit.hirex.security.dto.response.SignInRes;

public interface UserService {

//    User createUser(UserDto userDTO) throws Exception;

//    User updatePassword(String phoneNumber, String oldPassword, String newPassword) throws Exception;

    SignInRes login(String phoneNumber, String password) throws Exception;

//    User findByPhoneNumber(String phoneNumber);

}
