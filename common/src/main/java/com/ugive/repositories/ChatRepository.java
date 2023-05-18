package com.ugive.repositories;

import com.ugive.models.Chat;
import com.ugive.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Chat findByFirstUserAndSecondUser(User firstUser, User secondUser);

    @Query("SELECT c FROM Chat c WHERE c.firstUser.id = :userId OR c.secondUser.id = :userId")
    List<Chat> findAllByFirstUserIdOrSecondUserId(Long userId, Sort sort);

    @Modifying
    @Query("DELETE FROM Chat c WHERE c.isDeleted = true AND c.changed < :expirationDate")
    void deleteExpiredChat(@Param("expirationDate") Timestamp expirationDate);
}