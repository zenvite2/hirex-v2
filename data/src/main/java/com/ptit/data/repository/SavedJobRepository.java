package com.ptit.data.repository;

import com.ptit.data.entity.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    List<SavedJob> findAllByEmployeeId(Long id);

    void deleteByEmployeeIdAndJobId(Long id, Long jobId);
}
