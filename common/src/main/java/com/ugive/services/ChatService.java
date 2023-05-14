package com.ugive.services;

import com.ugive.dto.ChatDto;
import com.ugive.models.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    Optional<Chat> create(ChatDto chatDto);

    Optional<Chat> update(Long id, ChatDto chatDto);

    List<ChatDto> findAll(int page, int size);

    List<ChatDto> findAll();

    ChatDto findOne(Long id);

    List<ChatDto> getListOfChatsForUser(Long userId);

    void softDelete(Long id);
}
