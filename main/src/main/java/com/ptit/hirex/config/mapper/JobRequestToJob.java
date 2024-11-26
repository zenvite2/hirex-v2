package com.ptit.hirex.config.mapper;

import com.ptit.data.entity.Job;
import com.ptit.hirex.dto.request.JobRequest;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class JobRequestToJob extends PropertyMap<JobRequest, Job> {

    @Override
    protected void configure() {
        skip(destination.getId());
        skip(destination.getEmployer());
        map().setCityId(source.getCity());
        map().setDistrictId(source.getDistrict());
        map().setEducationLevelId(source.getEducation());
        map().setIndustryId(source.getIndustry());
        map().setJobTypeId(source.getJobType());
        map().setPositionId(source.getPosition());
        map().setContractTypeId(source.getContractType());
    }
}
