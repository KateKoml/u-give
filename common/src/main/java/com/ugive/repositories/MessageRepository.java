package com.ugive.repositories;

import com.ugive.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatIdAndTextContainingIgnoreCase(Long chatId, String textPart);

    List<Message> findAllByUserId(Long userId);

    List<Message> findAllByChatId(Long chatId);

    @Modifying
    @Query("DELETE FROM Message m WHERE m.isDeleted = true AND m.changed < :expirationDate")
    void deleteExpiredMessage(@Param("expirationDate") Timestamp expirationDate);
}