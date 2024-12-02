package com.ptit.hirex.security.service;

import com.ptit.data.entity.Employee;
import com.ptit.data.entity.User;
import com.ptit.data.repository.EmployeeRepository;
import com.ptit.data.repository.RoleRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.request.ForgotPasswordRequest;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.dto.request.ChangePasswordRequest;
import com.ptit.hirex.security.dto.request.SignInRequest;
import com.ptit.hirex.security.dto.request.SignUpRequest;
import com.ptit.hirex.security.dto.response.TokenResponse;
import com.ptit.hirex.service.LanguageService;
import com.ptit.hirex.service.MailService;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LanguageService languageService;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final MailService emailService;

    public ResponseEntity<ResponseDto<Object>> authenticate(SignInRequest signInRequest) {
        try {
            User user = userService.getByUserName(signInRequest.getUsername());

            if (user == null || !user.getActive()) {
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

            TokenResponse tokenResponse = TokenResponse.builder()
                    .accessToken(accessToken)
                    .username(user.getUsername())
                    .role(user.getRole().getName())
                    .userId(user.getId())
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
                    .role(roleRepository.findById(signUpRequest.getRoleId()).get())
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

    public ResponseEntity<ResponseDto<Object>> changePassword(ChangePasswordRequest request) {
        try {
            String username = getUserFromContext();
            if (username == null) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.unauthorized"),
                        StatusCodeEnum.AUTH0024
                );
            }

            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.user.not.found"),
                        StatusCodeEnum.AUTH0025
                );
            }
            User user = userOptional.get();

            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.password.old.incorrect"),
                        StatusCodeEnum.AUTH0026
                );
            }

            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                return ResponseBuilder.badRequestResponse(
                        languageService.getMessage("auth.password.same.as.old"),
                        StatusCodeEnum.AUTH0027
                );
            }

            String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
            user.setPassword(encodedNewPassword);

            userRepository.save(user);

            // 7. Có thể thêm logic gửi email thông báo đổi mật khẩu thành công
//            sendPasswordChangeNotification(user);

            return ResponseBuilder.okResponse(
                    languageService.getMessage("auth.password.changed.success"),
                    StatusCodeEnum.APPLICATION1000
            );

        } catch (Exception e) {
            log.error("Change password failed", e);
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.password.change.error"),
                    StatusCodeEnum.AUTH0028
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> processForgotPassword(ForgotPasswordRequest request) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseBuilder.badRequestResponse(
                        "email.not.found",
                        StatusCodeEnum.EMAIL0000
                );
            }

            User user = userOptional.get();

            String newPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            emailService.sendPasswordResetEmail(user.getEmail(), newPassword);

            return ResponseBuilder.okResponse(
                    "auth.reset.password.success",
                    StatusCodeEnum.AUTH0029
            );

        } catch (Exception e) {
            log.error("Password reset process failed", e);
            return ResponseBuilder.badRequestResponse(
                    "auth.reset.password.failed",
                    StatusCodeEnum.AUTH0030
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

    public Employee getEmployeeFromContext() {
        String username = getUserFromContext();

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            Employee employee = employeeRepository.findByUserId(user.get().getId());
            return employee;
        } else {
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
