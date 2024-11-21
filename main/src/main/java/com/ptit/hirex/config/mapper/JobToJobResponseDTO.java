package com.ptit.hirex.config.mapper;

import com.ptit.data.entity.Job;
import com.ptit.hirex.dto.response.JobDTO;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class JobToJobResponseDTO extends PropertyMap<Job, JobDTO> {

    @Override
    protected void configure() {
        map().setCity(source.getCityId());
        map().setDistrict(source.getDistrictId());
        map().setEducation(source.getEducationLevelId());
        map().setIndustry(source.getIndustryId());
        map().setJobType(source.getJobTypeId());
        map().setPosition(source.getPositionId());
        map().setContractType(source.getContractTypeId());
    }
}
