package com.ptit.hirex.service.impl;

import com.ptit.data.entity.Company;
import com.ptit.data.entity.Employer;
import com.ptit.data.entity.User;
import com.ptit.data.repository.CompanyRepository;
import com.ptit.data.repository.EmployerRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.UserInfoDto;
import com.ptit.hirex.dto.request.UserStatusRequest;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.LanguageService;
import com.ptit.hirex.service.MailService;
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
    private final MailService mailService;
    private final EmployerRepository employerRepository;
    private final CompanyRepository companyRepository;

    public ResponseEntity<ResponseDto<UserInfoDto>> getUserInfoById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseBuilder.noContentResponse(languageService.getMessage("user-info.not-found"), StatusCodeEnum.USERINFO0000);
        }
        String companyName = null;
        Employer employer = employerRepository.findByUserId(userId);
        if (employer != null) {
            Company company = companyRepository.findById(employer.getCompany()).orElse(null);
            if (company != null) {
                companyName = company.getCompanyName();
            }
        }
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .fullName(user.getFullName())
                .userId(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .companyName(companyName)
                .build();
        return ResponseBuilder.okResponse(languageService.getMessage("user-info.success"), userInfoDto, StatusCodeEnum.USERINFO1000);
    }

    @Override
    public ResponseEntity<ResponseDto<Object>> getAllUser() {
        try {
            List<User> list = userRepository.findAll();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("user-info.success"),
                    list,
                    StatusCodeEnum.USERINFO1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("user-info.success"),
                    StatusCodeEnum.USERINFO1000
            );
        }
    }

    @Override
    public ResponseEntity<ResponseDto<Object>> updateUser(Long userId, UserStatusRequest userStatusRequest) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseBuilder.noContentResponse(languageService.getMessage("user-info.not-found"), StatusCodeEnum.USERINFO0000);
            }

            user.setActive(userStatusRequest.getActive());

            userRepository.save(user);

            String mailInActive = "Chúng tôi xin thông báo rằng tài khoản của bạn đã bị **khóa tạm thời** bởi quản trị viên do vi phạm các điều khoản sử dụng. Vui lòng liên hệ với đội ngũ hỗ trợ của chúng tôi nếu bạn cần thêm thông tin chi tiết.\n" +
                    "\n" +
                    "Quản trị viên có thể yêu cầu bạn thực hiện các hành động cần thiết để mở lại tài khoản của mình.\n" +
                    "\n" +
                    "Chúng tôi rất tiếc vì sự bất tiện này.";

            String mailActive = "Chúng tôi xin thông báo rằng tài khoản của bạn đã được **mở khóa** bởi quản trị viên. Bây giờ bạn có thể truy cập lại tài khoản và sử dụng tất cả các dịch vụ của chúng tôi.\n" +
                    "\n" +
                    "Cảm ơn bạn đã hợp tác và tuân thủ các quy định của chúng tôi.";

            if (userStatusRequest.getActive()) {
                mailService.sendEmailCMS("Account Notification", user.getEmail(), user.getFullName(), mailActive, "https://deploy-hirexptit.io.vn/");
            } else {
                mailService.sendEmailCMS("Account Notification", user.getEmail(), user.getFullName(), mailInActive, "https://deploy-hirexptit.io.vn/");
            }

            return ResponseBuilder.okResponse(
                    languageService.getMessage("update.user-info.success"),
                    StatusCodeEnum.USERINFO1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("update.user-info.failed"),
                    StatusCodeEnum.USERINFO1000
            );
        }
    }
}
