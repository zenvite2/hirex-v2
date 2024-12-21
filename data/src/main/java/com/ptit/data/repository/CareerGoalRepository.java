package com.ptit.data.repository;

import com.ptit.data.entity.CareerGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CareerGoalRepository extends JpaRepository<CareerGoal, Long> {
    Optional<CareerGoal> findById(Long id);
    CareerGoal findByEmployeeId(Long id);
}
