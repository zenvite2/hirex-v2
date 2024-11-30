package com.ptit.data.repository;

import com.ptit.data.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByCompanyName(String name);

    Optional<Company> findByCompanyName(String name);

    Optional<Company> findById(Long id);
}
