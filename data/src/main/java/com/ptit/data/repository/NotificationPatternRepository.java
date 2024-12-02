package com.ptit.data.repository;

import com.ptit.data.entity.NotificationPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationPatternRepository extends JpaRepository<NotificationPattern, Long> {
    Optional<NotificationPattern> findByType(String type);

    @Query("SELECT noti from NotificationPattern noti where noti.patternCms = true")
    List<NotificationPattern> getAll();
}
