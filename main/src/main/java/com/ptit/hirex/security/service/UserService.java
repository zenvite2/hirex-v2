package com.ptit.hirex.security.service;


import com.ptit.data.base.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    UserDetailsService userDetailsService();

    User getByUserEmail(String email);

    User getByUserName(String userName);

}
