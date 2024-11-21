package com.ptit.data.repository;

import com.ptit.data.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findTop10BySenderIdOrReceiverIdOrderBySentTimeDesc(Long userId1, Long userId2);
}
