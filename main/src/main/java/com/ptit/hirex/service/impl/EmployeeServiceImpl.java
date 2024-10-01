package com.ptit.hirex.service.impl;

import com.ptit.data.base.User;
import com.ptit.data.entity.Employee;
import com.ptit.data.repository.EmployeeRepository;
import com.ptit.data.repository.RoleRepo;
import com.ptit.data.repository.UserRepo;
import com.ptit.hirex.dto.request.EmployeeRequest;
import com.ptit.hirex.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    @Override
    public void createEmployee(EmployeeRequest employeeRequest) {
        String phoneNumber = employeeRequest.getPhone();

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }

        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setMail(employeeRequest.getEmail());
        user.setFullName(employeeRequest.getFullName());
        user.setPassword(passwordEncoder.encode(employeeRequest.getPassword()));
        user.setRole(roleRepo.findById(1L).get());

        User userSave = userRepository.save(user);

        Employee employee = new Employee();
        employee.setUserId(userSave.getId());

        employeeRepository.save(employee);

    }
}
