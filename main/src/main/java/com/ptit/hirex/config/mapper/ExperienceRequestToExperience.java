package com.ptit.hirex.config.mapper;

import com.ptit.data.entity.Experience;
import com.ptit.hirex.dto.request.ExperienceRequest;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class ExperienceRequestToExperience extends PropertyMap<ExperienceRequest, Experience> {

    @Override
    protected void configure() {
//        map().setCompanyName(source.getCompanyName());
//        map().setPositionId(source.getPosition());
//        map().setExperienceNumber(source.getExperienceNumber());
//        map().setStartDate(source.getStartDate());
//        map().setEndDate(source.getEndDate());
//        map().setDescription(source.getDescription());
    }
}
