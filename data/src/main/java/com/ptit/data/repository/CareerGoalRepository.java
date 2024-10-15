package com.ptit.data.repository;

import com.ptit.data.entity.CareerGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerGoalRepository extends JpaRepository<CareerGoal, Long> {
    CareerGoal findByEmployeeId(Long id);
}
