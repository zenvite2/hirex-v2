package com.ptit.hirex.config.mapper;

import com.ptit.data.entity.CareerGoal;
import com.ptit.hirex.dto.request.CareerGoalRequest;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class CareerGoalRequestToCareerGoal extends PropertyMap<CareerGoalRequest, CareerGoal> {

    @Override
    protected void configure() {
        skip(destination.getId());
    }
}
