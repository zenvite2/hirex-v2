package com.ptit.data.repository;

import com.ptit.data.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findAllByEmployeeId(Long employeeId);
    Optional<Experience> findById(Long id);
}
