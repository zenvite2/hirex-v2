package com.ptit.hirex.service.impl;

import com.ptit.data.base.User;
import com.ptit.data.entity.Company;
import com.ptit.data.entity.Employer;
import com.ptit.data.repository.EmployerRepository;
import com.ptit.data.repository.RoleRepo;
import com.ptit.data.repository.UserRepo;
import com.ptit.hirex.dto.request.EmployerRequest;
import com.ptit.hirex.service.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;


    @Override
    public void createEmployer(EmployerRequest employerRequest) {
        String phoneNumber = employerRequest.getPhone();

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }

        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setMail(employerRequest.getEmail());
        user.setFullName(employerRequest.getFullName());
        user.setPassword(passwordEncoder.encode(employerRequest.getPassword()));
        user.setRole(roleRepo.findById(2L).get());

        User userSave = userRepository.save(user);

        Company company = new Company();

        Employer employer = new Employer();
        employer.setUserId(userSave.getId());
        employer.setCompanyId(company.getId());

        employerRepository.save(employer);

    }
}
