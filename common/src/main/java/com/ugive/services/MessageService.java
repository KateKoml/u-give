package com.ugive.services;

import com.ugive.requests.MessageRequest;
import com.ugive.models.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    Message create(MessageRequest messageRequest);

    Message update(Long id, MessageRequest messageRequest);

    List<Message> findAll();

    List<Message> findAllForOneUser(Long userId);

    List<Message> showAllMessagesInChat(Long chatId);

    List<Message> searchMessagesInChat(Long chatId, String textPart);

    Message markAsRead(Long id);

    Message findOne(Long id);

    void softDelete(Long id);
}
