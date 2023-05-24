package com.ugive.services;

import com.ugive.models.Chat;
import com.ugive.requests.ChatRequest;

import java.util.List;

public interface ChatService {
    Chat create(ChatRequest chatRequest);

    Chat update(Long id, ChatRequest chatRequest);

    List<Chat> findAll();

    Chat findOne(Long id);

    List<Chat> getListOfChatsForUser(Long userId);

    void softDelete(Long id);
}
