package com.ptit.data.repository;

import com.ptit.data.entity.YearExperience;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YearExperienceRepository extends JpaRepository<YearExperience, Long> {
    @Override
    List<YearExperience> findAll(Sort sort);
}
