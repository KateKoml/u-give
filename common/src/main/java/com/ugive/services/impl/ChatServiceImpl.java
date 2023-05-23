package com.ugive.services.impl;

import com.ugive.requests.ChatRequest;
import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.exceptions.ForbiddenChangeException;
import com.ugive.exceptions.ModifyingChatException;
import com.ugive.mappers.ChatMapper;
import com.ugive.models.Chat;
import com.ugive.models.User;
import com.ugive.repositories.ChatRepository;
import com.ugive.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.modelmapper.MappingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {
    private static final Logger logger = Logger.getLogger(ChatServiceImpl.class);
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    @Override
    @Transactional
    public Chat create(ChatRequest chatRequest) {
        Chat chat;
        try {
        chat = chatMapper.toEntity(chatRequest);
        } catch (MappingException e) {
            logger.error("Wrong mapping for entity. " + e.getMessage());
            throw new ForbiddenChangeException(e.getMessage());
        }
        if (isChatExistsBetweenUsers(chat.getSecondUser(), chat.getFirstUser())) {
            logger.error("This chat is already exists between these users.");
            throw new ModifyingChatException("This chat is already exists.");
        }
        return chatRepository.save(chat);
    }

    @Override
    @Transactional
    public Chat update(Long id, ChatRequest chatRequest) {
        Chat chat = chatCheck(id);
        if (isChatExistsBetweenUsers(chat.getSecondUser(), chat.getFirstUser())) {
            logger.error("This chat is already exists between these users.");
            throw new ModifyingChatException("This chat is already exists.");
        }
        try {
        chatMapper.updateEntityFromRequest(chatRequest, chat);
        } catch (ForbiddenChangeException e) {
            logger.error("Error updating chat request to entity. " + e.getMessage());
            throw new ForbiddenChangeException(e.getMessage());
        }
        return chatRepository.save(chat);
    }

    @Override
    public List<Chat> findAll() {
        return chatRepository.findAllByIsDeletedFalseOrderByCreated();
    }

    @Override
    public Chat findOne(Long id) {
        return chatCheck(id);
    }

    @Override
    public List<Chat> getListOfChatsForUser(Long userId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "changed");
        return chatRepository.findAllByFirstUserIdOrSecondUserId(userId, sort);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        Chat chat = chatCheck(id);
        chat.setDeleted(true);
        chat.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        chatRepository.save(chat);
    }

    @Scheduled(cron = "0 0 0 * * *") // Запускать каждый день в полночь,  "0 */1 * * * *" каждая минута
    @Transactional
    public void deleteExpiredChat() {
        Timestamp expirationDate = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
        chatRepository.deleteExpiredChat(expirationDate);
    }

    private Chat chatCheck(Long id) {
        Chat chat = chatRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This chat doesn't exist or was deleted."));
        if (chat.isDeleted()) {
            logger.error("Chat is deleted (isDeleted = true.");
            throw new ForbiddenChangeException("Chat is deleted");
        }
        return chat;
    }

    public boolean isChatExistsBetweenUsers(User firstUser, User secondUser) {
        Chat chat = chatRepository.findByFirstUserAndSecondUser(firstUser, secondUser);
        return chat != null;
    }
}
