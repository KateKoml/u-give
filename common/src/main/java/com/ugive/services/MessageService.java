package com.ugive.services;

import com.ugive.dto.MessageDto;
import com.ugive.models.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    Optional<Message> create(MessageDto messageDto);

    Optional<Message> update(Long id, MessageDto messageDto);

    List<MessageDto> findAll(int page, int size);

    List<MessageDto> findAll();

    List<MessageDto> findAllForOneUser(Long userId);

    List<MessageDto> showAllMessagesInChat(Long chatId);

    List<MessageDto> searchMessagesInChat(Long chatId, String textPart);

    Message markAsRead(Long id);

    MessageDto findOne(Long id);

    void softDelete(Long id);
}
