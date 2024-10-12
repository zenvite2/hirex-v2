package com.ptit.data.repository;

import com.ptit.data.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<City, Long> {
}
