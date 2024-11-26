package com.ptit.data.repository;

import com.ptit.data.entity.FollowCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowCompanyRepository extends JpaRepository<FollowCompany, Long> {
    List<FollowCompany> findAllByEmployeeId(Long id);

    void deleteByEmployeeIdAndCompanyId(Long id, Long companyId);
}
