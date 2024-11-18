package com.ptit.hirex.config.mapper;

import com.ptit.data.entity.Skill;
import com.ptit.hirex.dto.request.SkillRequest;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class SkillRequestToSkill extends PropertyMap<SkillRequest, Skill> {

    @Override
    protected void configure() {
        skip(destination.getId());
    }
}
