package com.ptit.data.repository;

import com.ptit.data.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    @Query(value = "SELECT * FROM district WHERE UNACCENT(name) ILIKE '%' || UNACCENT(?1) || '%'" +
            "ORDER BY \n" +
            "  CASE \n" +
            "    WHEN name LIKE '%' || ?1 || '%' THEN 0" +
            "    WHEN name ILIKE '%' || ?1 || '%' THEN 1" +
            "    WHEN unaccent(name) ILIKE '%' || unaccent(?1) || '%' THEN 2" +
            "    ELSE 3" +
            "  END,\n" +
            "  name", nativeQuery = true)
    List<District> findByNameContainingIgnoreCase(String name);

    List<District> findByCityIdIn(List<Long> districtIds);

    @Query(value = "SELECT * FROM district WHERE UNACCENT(name) ILIKE '%' || UNACCENT(?1) || '%' AND city_id IN (?2)" +
            "ORDER BY \n" +
            "  CASE \n" +
            "    WHEN name LIKE '%' || ?1 || '%' AND city_id IN (?2)THEN 0" +
            "    WHEN name ILIKE '%' || ?1 || '%'AND city_id IN (?2) THEN 1" +
            "    WHEN unaccent(name) ILIKE '%' || unaccent(?1) || '%' AND city_id IN (?2) THEN 2" +
            "    ELSE 3" +
            "  END,\n" +
            "  name", nativeQuery = true)
    List<District> findByNameContainingIgnoreCaseAndCountryIds(String name, List<Long> districtIds);

    @Query(value = "SELECT * FROM district WHERE UNACCENT(name) ILIKE '%' || UNACCENT(?1) || '%' AND city_id = ?2 " +
            "ORDER BY \n" +
            "  CASE \n" +
            "    WHEN name LIKE '%' || ?1 || '%' AND city_id = ?2 THEN 0 \n" +
            "    WHEN name ILIKE '%' || ?1 || '%' AND city_id = ?2 THEN 1 \n" +
            "    WHEN unaccent(name) ILIKE '%' || unaccent(?1) || '%' AND city_id = ?2 THEN 2 \n" +
            "    ELSE 3 \n" +
            "  END,\n" +
            "  name", nativeQuery = true)
    List<District> findByNameContainingIgnoreCaseAndCityId(String name, Long cityId);

    List<District> findByCityId(Long cityId);

}
