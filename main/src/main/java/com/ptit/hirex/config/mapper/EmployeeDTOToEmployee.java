package com.ptit.hirex.config.mapper;

import com.ptit.data.entity.Employee;
import com.ptit.hirex.dto.EmployeeDto;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDTOToEmployee extends PropertyMap<EmployeeDto, Employee> {

    @Override
    protected void configure() {
        map().setDateOfBirth(source.getDateOfBirth());
        map().setAddress(source.getAddress());
        map().setEmail(source.getEmail());
        map().setGender(source.getGender());
    }
}
