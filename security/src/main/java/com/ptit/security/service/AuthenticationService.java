package com.ptit.security.service;

import com.ptit.data.base.RedisToken;
import com.ptit.data.base.Role;
import com.ptit.data.base.User;
import com.ptit.data.repository.RoleRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.exception.DataNotFoundException;
import com.ptit.hirex.exception.InvalidDataException;
import com.ptit.security.dto.request.RefreshTokenRequest;
import com.ptit.security.dto.request.SignInRequest;
import com.ptit.security.dto.request.SignUpRequest;
import com.ptit.security.dto.response.TokenResponse;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

import static com.ptit.security.util.TokenType.REFRESH_TOKEN;
import static org.springframework.http.HttpHeaders.REFERER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
//    private final TokenService tokenService;
    private final UserService userService;
    private final JwtService jwtService;
    private final RedisTokenService redisTokenService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse authenticate(SignInRequest signInRequest) {
        log.info("---------- authenticate ----------");

        var user = userService.getByUserEmail(signInRequest.getEmail());

//        List<String> roles = userService.findAllRolesByUserId(user.getId());
//        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword(), authorities));
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
                signInRequest.getPassword(), user.getAuthorities());

        // authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        // create new access token
        String accessToken = jwtService.generateToken(user);

        // create new refresh token
        String refreshToken = jwtService.generateRefreshToken(user);

        // save token to db
//        tokenService.save(Token.builder().username(user.getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());
        redisTokenService.save(RedisToken.builder().id(user.getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    /**
     * Refresh token
     *
     * @param request
     * @return
     */
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        log.info("---------- refreshToken ----------");

        final String refreshToken = request.getRefreshToken();
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }
        final String userName = jwtService.extractUsername(refreshToken, REFRESH_TOKEN);
        var user = userService.getByUserEmail(userName);
        if (!jwtService.isValid(refreshToken, REFRESH_TOKEN, user)) {
            throw new InvalidDataException("Not allow access with this token");
        }

        // create new access token
        String accessToken = jwtService.generateToken(user);

        // save token to db
//        tokenService.save(Token.builder().username(user.getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());
        redisTokenService.save(RedisToken.builder().id(user.getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

//    @Override
    public User createUser(SignUpRequest signUpRequest) throws Exception {
        String email = signUpRequest.getEmail();
        // Kiểm tra xem số điện thoại đã tồn tại hay chưa
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        System.out.println("xxxxxxxx: " + signUpRequest.getRoleId());
        Role role = roleRepository.findById(signUpRequest.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
//		if (role.getName().toUpperCase().equals(Role.ADMIN)) {
//			throw new PermissionDenyException("You cannot register an admin account");
//		}
        System.out.println("xxxxxxxx111qqq");

        System.out.println("role: " + role.getName());

        // convert from userDTO => user
        User newUser = User.builder().email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .build();
        System.out.println();
        newUser.setRole(role);

        String password = signUpRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);
        return userRepository.save(newUser);
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
