package com.ugive.services;

import com.ugive.requests.ChatRequest;
import com.ugive.models.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    Chat create(ChatRequest chatRequest);

    Chat update(Long id, ChatRequest chatRequest);

    List<Chat> findAll();

    Chat findOne(Long id);

    List<Chat> getListOfChatsForUser(Long userId);

    void softDelete(Long id);
}
