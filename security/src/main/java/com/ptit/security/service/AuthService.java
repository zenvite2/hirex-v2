package com.ptit.security.service;

import com.ptit.data.entity.AccountInfo;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.security.config.UserDetailsImpl;
import com.ptit.security.dto.request.LoginRequest;
import com.ptit.security.dto.request.RegisterRequest;
import com.ptit.security.dto.response.AccountInfoResponse;
import com.ptit.security.dto.response.AuthResponse;
import com.ptit.security.repository.AccountInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final AccountInfoRepository accountInfoRepository;
    private final LanguageService languageService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final AccountInfoService accountInfoService;

    public ResponseEntity<ResponseDto<Object>> register(RegisterRequest registerRequest) {
        if (accountInfoRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.register.exists.email"),
                    StatusCodeEnum.AUTH0012);
        }

        AccountInfo savedAccount = accountInfoService.createNewAccountByEmail(registerRequest);
        return ResponseBuilder.okResponse(
                languageService.getMessage("auth.register.exists.email"),
                savedAccount,
                StatusCodeEnum.AUTH0012);
    }

    public ResponseEntity<ResponseDto<Object>> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            AccountInfoResponse accountInfoResponse = modelMapper.map(userDetails.getAccount(), AccountInfoResponse.class);

            String accessToken = jwtService.generateAccessToken(authentication);
            String refreshToken = jwtService.generateRefreshToken(authentication);

            AuthResponse authResponse = new AuthResponse(refreshToken, accessToken, accountInfoResponse);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("auth.login.success"),
                    authResponse,
                    StatusCodeEnum.AUTH1003);
        } catch (BadCredentialsException e) {
            log.error("Wrong email or password: {}", e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.login.bad.credentials"),
                    StatusCodeEnum.AUTH0011);
        } catch (Exception e) {
            log.error("Error in login: {}", e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.login.failed"),
                    StatusCodeEnum.AUTH0010);
        }
    }

}
