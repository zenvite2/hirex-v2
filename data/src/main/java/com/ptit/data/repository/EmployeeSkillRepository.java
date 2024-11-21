package com.ptit.data.repository;

import com.ptit.data.entity.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Long> {
    List<EmployeeSkill> findAllByEmployeeId(Long employeeId);
}
