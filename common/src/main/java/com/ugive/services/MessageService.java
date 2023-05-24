package com.ugive.services;

import com.ugive.models.Message;
import com.ugive.requests.MessageRequest;

import java.util.List;

public interface MessageService {
    Message create(MessageRequest messageRequest);

    Message update(Long id, MessageRequest messageRequest);

    List<Message> findAll();

    List<Message> findAllForOneUserSorted(Long userId);

    List<Message> showAllMessagesInChatSorted(Long chatId);

    List<Message> searchMessagesInChat(Long chatId, String textPart);

    Message markAsRead(Long id);

    Message findOne(Long id);

    void softDelete(Long id);
}
