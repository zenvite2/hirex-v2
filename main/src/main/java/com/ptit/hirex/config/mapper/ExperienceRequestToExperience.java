package com.ptit.hirex.config.mapper;

import com.ptit.data.entity.Experience;
import com.ptit.hirex.dto.request.ExperienceRequest;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class ExperienceRequestToExperience extends PropertyMap<ExperienceRequest, Experience> {

    @Override
    protected void configure() {
        skip(destination.getId());
        skip(destination.getEmployeeId());
    }
}
