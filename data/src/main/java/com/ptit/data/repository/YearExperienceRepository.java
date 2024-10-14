package com.ptit.data.repository;

import com.ptit.data.entity.YearExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YearExperienceRepository extends JpaRepository<YearExperience, Long> {
}
