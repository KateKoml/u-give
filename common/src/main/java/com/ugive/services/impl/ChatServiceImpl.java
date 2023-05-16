package com.ugive.services.impl;

import com.ugive.dto.ChatDto;
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
    public Optional<Chat> create(ChatDto chatDto) {
        Chat chat = chatMapper.toEntity(chatDto);
        if (isChatExistsBetweenUsers(chat.getSecondUser(), chat.getFirstUser())) {
            throw new ModifyingChatException("This chat is already exists.");
        }
        return Optional.of(chatRepository.save(chat));
    }

    @Override
    public Optional<Chat> update(Long id, ChatDto chatDto) {
        Chat chat = chatCheck(id);
        if (Boolean.TRUE.equals(chat.getIsDeleted())) {
            throw new ForbiddenChangeException("Chat is deleted");
        }
        if (isChatExistsBetweenUsers(chat.getSecondUser(), chat.getFirstUser())) {
            throw new ModifyingChatException("This chat is already exists.");
        }
        chatMapper.updateEntityFromDto(chatDto, chat);
        return Optional.of(chatRepository.save(chat));
    }

    @Override
    public List<ChatDto> findAll(int page, int size) {
        Page<Chat> chatsPage = chatRepository.findAll(PageRequest.of(page, size, Sort.by("created")));
        return chatsPage.getContent().stream()
                .map(chatMapper::toDto)
                .toList();
    }

    @Override
    public List<ChatDto> findAll() {
        List<Chat> chats = chatRepository.findAll();
        return chats.stream().map(chatMapper::toDto).toList();
    }

    @Override
    public ChatDto findOne(Long id) {
        Chat chat = chatCheck(id);
        if (Boolean.TRUE.equals(chat.getIsDeleted())) {
            throw new ForbiddenChangeException("Chat is deleted");
        }
        return chatMapper.toDto(chat);
    }

    @Override
    public List<ChatDto> getListOfChatsForUser(Long userId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "changed");
        List<Chat> chats = chatRepository.findAllByFirstUserIdOrSecondUserId(userId, sort);
        return chats.stream()
                .map(chatMapper::toDto)
                .toList();
    }

    @Override
    public void softDelete(Long id) {
        Chat chat = chatCheck(id);
        chat.setIsDeleted(true);
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
        return chatRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This chat doesn't exist or was deleted."));
    }

    public boolean isChatExistsBetweenUsers(User firstUser, User secondUser) {
        Chat chat = chatRepository.findByFirstUserAndSecondUser(firstUser, secondUser);
        return chat != null;
    }
}