package com.ptit.data.repository;

import com.ptit.data.entity.Salary;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    @Override
    List<Salary> findAll(Sort sort);
}
