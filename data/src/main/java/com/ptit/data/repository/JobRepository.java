package com.ptit.data.repository;

import com.ptit.data.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByEmployer(Long employerId);

    @Query("SELECT j FROM Job j WHERE " +
            "(:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:city IS NULL OR j.city = :city) " +
            "AND (:experience IS NULL OR j.yearExperience IN :experience) " +
            "AND (:salary IS NULL OR j.salary IN :salary) " +
            "AND (:tech IS NULL OR j.tech IN :tech) " +
            "AND (:jobType IS NULL OR j.jobType IN :jobType) " +
            "AND (:position IS NULL OR j.position IN :position) " +
            "AND (:contractType IS NULL OR j.contractType IN :contractType)")
    List<Job> searchJobs(@Param("title") String title,
                         @Param("city") Long city,
                         @Param("experience") List<Long> experience,
                         @Param("tech") List<Long> tech,
                         @Param("salary") List<Long> salary,
                         @Param("jobType") List<Long> jobType,
                         @Param("position") List<Long> position,
                         @Param("contractType") List<Long> contractType);

}