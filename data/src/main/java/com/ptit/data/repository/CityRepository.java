package com.ptit.data.repository;

import com.ptit.data.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query(value = "SELECT * FROM city WHERE UNACCENT(name) ILIKE '%' || UNACCENT(?1) || '%'" +
            "ORDER BY \n" +
            "  CASE \n" +
            "    WHEN name LIKE '%' || ?1 || '%' THEN 0" +
            "    WHEN name ILIKE '%' || ?1 || '%' THEN 1" +
            "    WHEN unaccent(name) ILIKE '%' || unaccent(?1) || '%' THEN 2" +
            "    ELSE 3" +
            "  END,\n" +
            "  name", nativeQuery = true)
    List<City> findByNameContainingIgnoreCase(String name);
}
