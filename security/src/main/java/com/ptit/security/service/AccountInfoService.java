package com.ptit.security.service;

import com.ptit.data.entity.AccountInfo;
import com.ptit.security.dto.request.RegisterRequest;
import com.ptit.security.repository.AccountInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountInfoService {

    private final PasswordEncoder passwordEncoder;
    private final AccountInfoRepository accountInfoRepository;

    @Transactional(rollbackFor = Exception.class)
    public AccountInfo createNewAccountByEmail(RegisterRequest registerRequest) {
        AccountInfo accountInfoModel = new AccountInfo();

        accountInfoModel.setEmail(registerRequest.getEmail());
        accountInfoModel.setHashPassword(passwordEncoder.encode(registerRequest.getPassword()));

        AccountInfo newAccountInfoModel;

        try {
            newAccountInfoModel = accountInfoRepository.save(accountInfoModel);
        } catch (Exception e) {
            log.error("Create account info by email failed: {}", e.getMessage());
            throw e;
        }

//        createNewCompanyByAccount(newAccountInfoModel.getId()); create employee ho

        return newAccountInfoModel;
    }
}
