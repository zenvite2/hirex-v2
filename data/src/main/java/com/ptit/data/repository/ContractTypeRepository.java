package com.ptit.data.repository;

import com.ptit.data.entity.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractTypeRepository extends JpaRepository<ContractType, Long> {
}
