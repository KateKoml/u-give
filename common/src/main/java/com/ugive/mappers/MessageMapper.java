package com.ugive.mappers;

import com.ugive.dto.MessageDto;
import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.models.Message;
import com.ugive.repositories.ChatRepository;
import com.ugive.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class MessageMapper {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ChatRepository chatRepository;

    public Message toEntity(MessageDto messageDto) {
        Message message = modelMapper.map(messageDto, Message.class);
        message.setChat(chatRepository.findById(messageDto.getPrivateChat()).orElseThrow(() -> new EntityNotFoundException("Chat not found.")));
        message.setUser(userService.findOne(messageDto.getSender()));
        return message;
    }

    public MessageDto toDto(Message message) {
        MessageDto messageDto = modelMapper.map(message, MessageDto.class);
        messageDto.setPrivateChat(message.getChat().getId());
        messageDto.setSender(message.getUser().getId());
        return messageDto;
    }

    public void updateEntityFromDto(MessageDto messageDto, Message message) {
        if (messageDto.getPrivateChat() != null) {
            message.setChat(chatRepository.findById(messageDto.getPrivateChat()).orElseThrow(() -> new EntityNotFoundException("Chat not found.")));
        }
        if (messageDto.getSender() != null) {
            message.setUser(userService.findOne(messageDto.getSender()));
        }
        if (messageDto.getText() != null) {
            message.setText(messageDto.getText());
        }
        message.setChanged(Timestamp.valueOf(LocalDateTime.now()));
    }
}
