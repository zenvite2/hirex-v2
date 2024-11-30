package com.ptit.hirex.config.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new EmployeeDTOToEmployee());
        modelMapper.addMappings(new ExperienceRequestToExperience());
        modelMapper.addMappings(new JobRequestToJob());
        modelMapper.addMappings(new CareerGoalRequestToCareerGoal());
        modelMapper.addMappings(new EducationRequestToEducation());
        modelMapper.addMappings(new SkillRequestToSkill());
        return modelMapper;
    }
}