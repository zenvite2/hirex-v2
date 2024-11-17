package com.ptit.hirex.dto;


import com.ptit.data.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDetailDto {
    private int employeeId;
    private Employee employeeInfo;
    private List<Skill> lstSkills;
    private List<Experience> lstExperiences;
    private List<Education> lstEducations;
    private List<CareerGoal> lstCareerGoals;
}
