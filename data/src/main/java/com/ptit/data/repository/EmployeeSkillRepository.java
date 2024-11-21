package com.ptit.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ptit.data.entity.EmployeeSkill;
import java.util.List;

public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Long> {
    List<EmployeeSkill> findByEmployeeId(Long employeeId);
}
