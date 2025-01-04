package com.ptit.data.repository;

import com.ptit.data.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByEmployeeId(Long employeeId);
    @Query("select count(a) from Application a where a.jobId = :jobId")
    Integer countApplication(@Param("jobId") Long jobId);

}
