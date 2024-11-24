package com.ptit.data.repository;

import com.ptit.data.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Query(value = "SELECT * FROM skill WHERE UNACCENT(name) ILIKE '%' || UNACCENT(?1) || '%'" +
            "ORDER BY \n" +
            "  CASE \n" +
            "    WHEN name LIKE '%' || ?1 || '%' THEN 0" +
            "    WHEN name ILIKE '%' || ?1 || '%' THEN 1" +
            "    WHEN unaccent(name) ILIKE '%' || unaccent(?1) || '%' THEN 2" +
            "    ELSE 3" +
            "  END,\n" +
            "  name", nativeQuery = true)
    List<Skill> findByNameContainingIgnoreCase(String name);
}
