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
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    @Override
    @Transactional
    public Chat create(ChatRequest chatRequest) {
        Chat chat = chatMapper.toEntity(chatRequest);
        if (isChatExistsBetweenUsers(chat.getSecondUser(), chat.getFirstUser())) {
            throw new ModifyingChatException("This chat is already exists.");
        }
        return chatRepository.save(chat);
    }

    @Override
    @Transactional
    public Chat update(Long id, ChatRequest chatRequest) {
        Chat chat = chatCheck(id);
        if (isChatExistsBetweenUsers(chat.getSecondUser(), chat.getFirstUser())) {
            throw new ModifyingChatException("This chat is already exists.");
        }
        chatMapper.updateEntityFromRequest(chatRequest, chat);
        return chatRepository.save(chat);
    }

    @Override
    public List<Chat> findAll() {

        return chatRepository.findAll().stream().filter(chat -> !chat.isDeleted()).toList();
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
            throw new ForbiddenChangeException("Chat is deleted");
        }
        return chat;
    }

    public boolean isChatExistsBetweenUsers(User firstUser, User secondUser) {
        Chat chat = chatRepository.findByFirstUserAndSecondUser(firstUser, secondUser);
        return chat != null;
    }
}
