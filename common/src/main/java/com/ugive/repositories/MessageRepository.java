package com.ugive.repositories;

import com.ugive.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.user.id = :userId AND m.isDeleted = false ORDER BY m.created DESC")
    List<Message> findAllForOneUser(@Param("userId") Long userId);

    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId AND m.isDeleted = false ORDER BY m.created DESC")
    List<Message> findAllMessagesInChatSorted(@Param("chatId") Long chatId);

    @Modifying
    @Query("DELETE FROM Message m WHERE m.isDeleted = true AND m.changed < :expirationDate")
    void deleteExpiredMessage(@Param("expirationDate") Timestamp expirationDate);

    List<Message> findAllByDeletedFalse();
}