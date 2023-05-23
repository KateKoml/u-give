package com.ugive.services.impl;

import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.exceptions.ForbiddenChangeException;
import com.ugive.mappers.MessageMapper;
import com.ugive.models.Chat;
import com.ugive.models.Message;
import com.ugive.repositories.ChatRepository;
import com.ugive.repositories.MessageRepository;
import com.ugive.requests.MessageRequest;
import com.ugive.services.EncryptionService;
import com.ugive.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.modelmapper.MappingException;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
    private static final Logger logger = Logger.getLogger(MessageServiceImpl.class);
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final EncryptionService encryptionService;

    @Override
    @Transactional
    public Message create(MessageRequest messageDto) {
        Message message;
        try {
        message = messageMapper.toEntity(messageDto);
        } catch (MappingException e) {
            logger.error("Wrong mapping for entity. " + e.getMessage());
            throw new ForbiddenChangeException(e.getMessage());
        }
        Chat chat = chatRepository.findById(messageDto.getPrivateChat()).orElseThrow(() -> new EntityNotFoundException("Chat not found."));

        //Check if the sender is a member of the chat
        if (!chat.getFirstUser().equals(message.getUser()) && !chat.getSecondUser().equals(message.getUser())) {
            throw new ForbiddenChangeException("This user is not a member of this chat");
        }

        String encrypt = encryptionService.encrypt(message.getText());
        message.setText(encrypt);

        return messageRepository.save(message);
    }

    @Override
    @Transactional
    public Message update(Long id, MessageRequest messageDto) {
        Message message = messageCheck(id);
        try {
        messageMapper.updateEntityFromRequest(messageDto, message);
        } catch (ForbiddenChangeException e) {
            logger.error("Error updating message request to entity. " + e.getMessage());
            throw new ForbiddenChangeException(e.getMessage());
        }
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = messageRepository.findAllByDeletedFalse();
        for (Message message : messages) {
            String decryptMessage = encryptionService.decrypt(message.getText());
            message.setText(decryptMessage);
        }
        return messages;
    }

    @Override
    public List<Message> findAllForOneUserSorted(Long userId) {
        List<Message> messages = messageRepository.findAllForOneUser(userId);
        for (Message message : messages) {
            String decryptMessage = encryptionService.decrypt(message.getText());
            message.setText(decryptMessage);
        }
        return messages;
    }

    @Override
    public List<Message> showAllMessagesInChatSorted(Long chatId) {
        List<Message> messages = messageRepository.findAllMessagesInChatSorted(chatId);
        for (Message message : messages) {
            String decryptMessage = encryptionService.decrypt(message.getText());
            message.setText(decryptMessage);
        }
        return messages;
    }

    @Override
    public List<Message> searchMessagesInChat(Long chatId, String textPart) {
        List<Message> messages = messageRepository.findAllMessagesInChatSorted(chatId);
        List<Message> searchResult = new ArrayList<>();
        for (Message message : messages) {
            String decryptMessage = encryptionService.decrypt(message.getText());
            message.setText(decryptMessage);
            if (decryptMessage.contains(textPart)) {
                searchResult.add(message);
            }
        }
        return searchResult;
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
        Message message = messageCheck(id);
        String decryptMessage = encryptionService.decrypt(message.getText());
        message.setText(decryptMessage);
        return message;
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
        try {
        Timestamp expirationDate = Timestamp.valueOf(LocalDateTime.now().minusDays(10));
        messageRepository.deleteExpiredMessage(expirationDate);
        } catch (SchedulingException e) {
            logger.error("Scheduling doesn't work correctly. " + e.getMessage());
        }
    }

    private Message messageCheck(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This message doesn't exist or was deleted."));
        if (message.isDeleted()) {
            logger.error("Message is deleted (isDeleted = true)");
            throw new EntityNotFoundException("This message was deleted.");
        }
        return message;
    }
}
