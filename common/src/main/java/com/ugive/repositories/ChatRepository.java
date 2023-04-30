package com.ugive.repositories;

import com.ugive.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ChatRepository extends
        JpaRepository<Chat, Long>,
        PagingAndSortingRepository<Chat, Long>,
        CrudRepository<Chat, Long> {
}