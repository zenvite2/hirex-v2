package com.ptit.security.repository;

import com.ptit.data.entity.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long> {
    boolean existsByEmail(String email);
}
