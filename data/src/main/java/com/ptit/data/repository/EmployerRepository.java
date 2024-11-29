package com.ptit.data.repository;

import com.ptit.data.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
    Employer findByUserId(Long userId);

    Employer findById(long id);

    List<Employer> findAllByCompany(Long id);
}
