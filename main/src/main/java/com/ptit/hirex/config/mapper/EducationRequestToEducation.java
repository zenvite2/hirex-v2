package com.ptit.hirex.config.mapper;

import com.ptit.data.entity.Education;
import com.ptit.hirex.dto.request.EducationRequest;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class EducationRequestToEducation extends PropertyMap<EducationRequest, Education> {

    @Override
    protected void configure() {
        skip(destination.getId());
        skip(destination.getEmployeeId());
    }
}
