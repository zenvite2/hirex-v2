package com.ptit.hirex.security.service;

import com.ptit.data.base.RedisToken;
import com.ptit.data.entity.User;
import com.ptit.data.entity.Employee;
import com.ptit.data.repository.EmployeeRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.dto.request.RefreshTokenRequest;
import com.ptit.hirex.security.dto.request.SignInRequest;
import com.ptit.hirex.security.dto.request.SignUpRequest;
import com.ptit.hirex.security.dto.response.TokenResponse;
import com.ptit.hirex.service.LanguageService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ptit.hirex.security.util.TokenType.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final RedisTokenService redisTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LanguageService languageService;
    private final EmployeeRepository employeeRepository;

    public ResponseEntity<ResponseDto<Object>> authenticate(SignInRequest signInRequest) {
        try {
            var user = userService.getByUserName(signInRequest.getUsername());

            if (user == null) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.signin.invalid.credentials"),
                        StatusCodeEnum.AUTH0013
                );
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    signInRequest.getUsername(),
                    signInRequest.getPassword(),
                    user.getAuthorities()
            );

            try {
                authenticationManager.authenticate(authenticationToken);
            } catch (BadCredentialsException e) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.signin.invalid.credentials"),
                        null,
                        StatusCodeEnum.AUTH0013
                );
            }

            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            try {
                redisTokenService.save(RedisToken.builder()
                        .id(user.getUsername())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build());
            } catch (Exception e) {
                log.error("Failed to save token to Redis", e);
            }

            TokenResponse tokenResponse = TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .username(user.getUsername())
                    .role(user.getRole().getName())
                    .build();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("auth.signin.success"),
                    tokenResponse,
                    StatusCodeEnum.AUTH0012
            );
        } catch (UsernameNotFoundException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.signin.invalid.credentials"),
                    null,
                    StatusCodeEnum.AUTH0013
            );
        } catch (Exception e) {
            log.error("Authentication failed", e);
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.signin.error"),
                    null,
                    StatusCodeEnum.AUTH0014
            );
        }
    }

    /**
     * Refresh token
     *
     * @param request
     * @return
     */

    public ResponseEntity<ResponseDto<Object>> refreshToken(RefreshTokenRequest request) {
        try {
            final String refreshToken = request.getRefreshToken();
            if (StringUtils.isBlank(refreshToken)) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.refresh.invalid.token"),
                        StatusCodeEnum.AUTH0015
                );
            }

            final String userName = jwtService.extractUsername(refreshToken, REFRESH_TOKEN);
            var user = userService.getByUserEmail(userName);

            if (user == null) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.refresh.user.not.found"),
                        StatusCodeEnum.AUTH0016
                );
            }

            if (!jwtService.isValid(refreshToken, REFRESH_TOKEN, user)) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.refresh.token.invalid"),
                        StatusCodeEnum.AUTH0015
                );
            }

            String accessToken = jwtService.generateToken(user);

            try {
                redisTokenService.save(RedisToken.builder()
                        .id(user.getUsername())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build());
            } catch (Exception e) {
                log.error("Failed to save token to Redis", e);
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.refresh.token.save.error"),
                        StatusCodeEnum.AUTH0017
                );
            }

            TokenResponse tokenResponse = TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .username(user.getUsername())
                    .build();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("auth.refresh.success"),
                    tokenResponse,
                    StatusCodeEnum.AUTH0012
            );

        } catch (Exception e) {
            log.error("Refresh token failed", e);
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.refresh.error"),
                    StatusCodeEnum.AUTH0018
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> createUser(SignUpRequest signUpRequest) {
        try {
            String userName = signUpRequest.getUsername();

            if (userRepository.existsByUsername(userName)) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.signup.user.exists"),
                        StatusCodeEnum.AUTH0019
                );
            }

            User newUser = User.builder()
                    .email(signUpRequest.getEmail())
                    .username(signUpRequest.getUsername())
                    .password(signUpRequest.getPassword())
                    .build();

            String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
            newUser.setPassword(encodedPassword);

            User userSave;
            try {
                userSave = userRepository.save(newUser);
            } catch (DataIntegrityViolationException e) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.signup.save.error"),
                        StatusCodeEnum.AUTH0021
                );
            }

            return ResponseBuilder.okResponse(
                    languageService.getMessage("auth.signup.success"),
                    userSave,
                    StatusCodeEnum.AUTH0022
            );

        } catch (Exception e) {
            log.error("User creation failed", e);
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.signup.error"),
                    StatusCodeEnum.AUTH0023
            );
        }
    }

    public String getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("Get user from context failed");
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public Long getEmployeeFromContext() {
        String username = getUserFromContext();

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            Employee employee = employeeRepository.findByUserId(user.get().getId());
            return employee.getId();
        }else{
            return null;
        }
    }

    /**
     * Logout
     *
     * @param request
     * @return
     */
//    public String logout(HttpServletRequest request) {
//        log.info("---------- logout ----------");
//
//        final String token = request.getHeader(REFERER);
//        if (StringUtils.isBlank(token)) {
//            throw new InvalidDataException("Token must be not blank");
//        }
//
//        final String userName = jwtService.extractUsername(token, ACCESS_TOKEN);
//        tokenService.delete(userName);
//
//        return "Deleted!";
//    }
}
