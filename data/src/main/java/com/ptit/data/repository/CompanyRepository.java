package com.ptit.data.repository;

import com.ptit.data.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByCompanyName(String name);

    <Optional> Company findByCompanyName(String name);
}
