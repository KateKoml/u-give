package com.ugive.repositories;

import com.ugive.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepository extends
        JpaRepository<Message, Long>,
        PagingAndSortingRepository<Message, Long>,
        CrudRepository<Message, Long> {
}