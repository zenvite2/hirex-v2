package com.ptit.data.repository;

import com.ptit.data.entity.Tech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechRepository extends JpaRepository<Tech, Long> {
}
