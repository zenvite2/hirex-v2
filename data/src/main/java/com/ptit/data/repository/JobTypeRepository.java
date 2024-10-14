package com.ptit.data.repository;

import com.ptit.data.entity.JobType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTypeRepository extends JpaRepository<JobType, Long> {
}
