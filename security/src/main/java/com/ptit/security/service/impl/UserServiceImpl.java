package com.ptit.security.service.impl;

import java.text.ParseException;
import java.util.Optional;

import com.ptit.data.base.User;
import com.ptit.data.repository.RoleRepo;
import com.ptit.data.repository.UserRepo;
import com.ptit.hirex.exception.ApiException;
import com.ptit.security.dto.request.SignInReq;
import com.ptit.security.dto.response.SignInRes;
import com.ptit.security.service.UserService;
import com.ptit.security.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final RoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public User findById(Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        return userOptional.orElse(null);
    }

//    @Override
//    public UserDto createUser(SignUpReq signUpReq) throws Exception {
//        String phoneNumber = signUpReq.getPhoneNumber();
//
//        if (userRepo.existsByPhoneNumber(phoneNumber)) {
//            throw new DataIntegrityViolationException("Phone number already exists");
//        }
//
//        Role role = roleRepository.findById(signUpReq.getRoleId())
//                .orElseThrow(() -> new ApiException(404, "Role not found"));
//		if (role.getName().toUpperCase().equals(Role.ADMIN)) {
//			throw new ApiException(401, "Cannot create admin account");
//		}
//
//        // convert from signUpReq => user
//        User newUser = User.builder().fullName(signUpReq.getFullName()).phoneNumber(signUpReq.getPhoneNumber())
//                .password(signUpReq.getPassword()).address(signUpReq.getAddress()).dateOfBirth(signUpReq.getDateOfBirth())
//                .build();
//        newUser.setRole(role);
//
////        // Kiểm tra nếu có accountId, không yêu cầu password
////        if (signUpReq.getFacebookAccountId() == 0 && signUpReq.getGoogleAccountId() == 0) {
////            String password = signUpReq.getPassword();
////            String encodedPassword = passwordEncoder.encode(password);
////            newUser.setPassword(encodedPassword);
////        }
//        return modelMapper.map(userRepo.save(newUser), UserDto.class);
//    }

    @Override
    public SignInRes login(SignInReq signInReq) throws Exception {
        Optional<User> optionalUser = userRepo.findByPhoneNumber(signInReq.getPhoneNumber());
        if (optionalUser.isEmpty()) {
            throw new ApiException(404, "User not found");
        }
        User existingUser = optionalUser.get();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInReq.getPhoneNumber(),
                signInReq.getPassword(), existingUser.getAuthorities());

        // authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);

        String token = jwtUtil.generateToken(existingUser);
        String role = existingUser.getRole().getName();
        Long id = existingUser.getId();
        String name = existingUser.getFullName();

//        if (role.toUpperCase().equals("USER")) {
//            Employee employee = employeeServiceImpl.getEmployee(optionalUser.get().getId());
//        } else {
//            Employer employer = employerServiceImpl.getEmployer(optionalUser.get().getId());
//        }

        return SignInRes.builder()
                        .fullname(name)
                        .id(id)
                        .role(role)
                        .token(token)
                        .build();
    }

    @Override
    public User getUserFromContext() {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            log.error("Failed to get user ID from security context: {}", e.getMessage());
            return null;
        }
    }


//    @Override
//    public User findByPhoneNumber(String phoneNumber) {
//        Optional<User> user = userRepo.findByPhoneNumber(phoneNumber);
//        if (user.isPresent()) {
//            return user.get();
//        }
//        return null;
//    }

//    @Override
//    public User updatePassword(String phoneNumber, String oldPassword, String newPassword) {
//        Optional<User> optionalUser = userRepo.findByPhoneNumber(phoneNumber);
//        if (optionalUser.isEmpty()) {
//            throw new ApiException(404, "Empty username or password");
//        }
//        User user = optionalUser.get();
//
//        // Kiểm tra mật khẩu cũ
//        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
//            throw new BadCredentialsException("Mật khẩu cũ không chính xác");
//        }
//
//        // Kiểm tra và cập nhật mật khẩu mới
//        if (!newPassword.equals(oldPassword)) {
//            // Mã hóa mật khẩu mới
//            String encodedNewPassword = passwordEncoder.encode(newPassword);
//            user.setPassword(encodedNewPassword);
//            // Lưu thay đổi vào cơ sở dữ liệu
//            userRepo.save(user);
//            return user;
//        } else {
//            throw new IllegalArgumentException("New password must be different from old password");
//        }
//    }

//    public User updateUser(UserUpdateDTO userUpdateDTO) throws Exception {
//        String fullName = userUpdateDTO.getFullName();
//        String address = userUpdateDTO.getAddress();
//        String mail = userUpdateDTO.getMail();
//        String dateOfBirth = userUpdateDTO.getDateOfBirth();
//        String gender = userUpdateDTO.getGender();
//
//        Optional<User> optionalUser = userRepo.findByPhoneNumber(userUpdateDTO.getPhoneNumber());
//
//        if (optionalUser.isEmpty()) {
//            throw new DataNotFoundException("User not found");
//        }
//        User user = optionalUser.get();
//
//        if (fullName != null) {
//            user.setFullName(fullName);
//        }
//        if (address != null) {
//            user.setAddress(address);
//        }
//        if (mail != null) {
//            user.setMail(mail);
//        }
//        if (dateOfBirth != null) {
//            user.setDateOfBirth(dateOfBirth);
//        }
//        if (gender != null) {
//            user.setGender(gender);
//        }
//        User updatedUser = userRepo.save(user);
//
//        return updatedUser;
//    }

}