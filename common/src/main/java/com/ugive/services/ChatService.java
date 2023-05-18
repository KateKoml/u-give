package com.ugive.services;

import com.ugive.dto.ChatRequest;
import com.ugive.models.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    Optional<Chat> create(ChatRequest chatRequest);

    Optional<Chat> update(Long id, ChatRequest chatRequest);

    List<Chat> findAll(int page, int size);

    List<Chat> findAll();

    Chat findOne(Long id);

    List<Chat> getListOfChatsForUser(Long userId);

    void softDelete(Long id);
}
