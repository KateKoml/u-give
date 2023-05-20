package com.ugive.services.impl;

import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.exceptions.ForbiddenChangeException;
import com.ugive.mappers.MessageMapper;
import com.ugive.models.Chat;
import com.ugive.models.Message;
import com.ugive.repositories.ChatRepository;
import com.ugive.repositories.MessageRepository;
import com.ugive.requests.MessageRequest;
import com.ugive.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public Optional<Message> create(MessageRequest messageDto) {
        Message message = messageMapper.toEntity(messageDto);
        Chat chat = chatRepository.findById(messageDto.getPrivateChat()).orElseThrow(() -> new EntityNotFoundException("Chat not found."));

        // Check if the sender is a member of the chat
        if (!chat.getFirstUser().equals(message.getUser()) && !chat.getSecondUser().equals(message.getUser())) {
            throw new ForbiddenChangeException("This user is not a member of this chat");
        }
        return Optional.of(messageRepository.save(message));
    }

    @Override
    @Transactional
    public Optional<Message> update(Long id, MessageRequest messageDto) {
        Message message = messageCheck(id);
        messageMapper.updateEntityFromRequest(messageDto, message);
        return Optional.of(messageRepository.save(message));
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> findAllForOneUser(Long userId) {
        List<Message> messages = messageRepository.findAllByUserId(userId);
        return messages.stream()
                .filter(message -> !message.isDeleted())
                .sorted(Comparator.comparing(Message::getCreated).reversed())
                .toList();
    }

    @Override
    public List<Message> showAllMessagesInChat(Long chatId) {
        List<Message> messages = messageRepository.findAllByChatId(chatId);
        return messages.stream()
                .filter(message -> !message.isDeleted())
                .sorted(Comparator.comparing(Message::getCreated).reversed())
                .toList();
    }

    @Override
    public List<Message> searchMessagesInChat(Long chatId, String textPart) {
        List<Message> messages = messageRepository.findByChatIdAndTextContainingIgnoreCase(chatId, textPart);
        return messages.stream()
                .filter(message -> !message.isDeleted())
                .sorted(Comparator.comparing(Message::getCreated).reversed())
                .toList();
    }

    @Override
    @Transactional
    public Message markAsRead(Long id) {
        Message message = messageCheck(id);
        message.setIsRead(true);
        return messageRepository.save(message);
    }

    @Override
    public Message findOne(Long id) {
        return messageCheck(id);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        Message message = messageCheck(id);
        message.setDeleted(true);
        message.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        messageRepository.save(message);
    }

    @Scheduled(cron = "0 0 0 * * *") // Запускать каждый день в полночь,  "0 */1 * * * *" каждая минута
    @Transactional
    public void deleteExpiredMessage() {
        Timestamp expirationDate = Timestamp.valueOf(LocalDateTime.now().minusDays(10));
        messageRepository.deleteExpiredMessage(expirationDate);
    }

    private Message messageCheck(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This message doesn't exist or was deleted."));
        if (message.isDeleted()) {
            throw new EntityNotFoundException("This message was deleted.");
        }
        return message;
    }
}
