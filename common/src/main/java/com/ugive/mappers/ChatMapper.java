package com.ugive.mappers;

import com.ugive.dto.ChatDto;
import com.ugive.models.Chat;
import com.ugive.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class ChatMapper {
    private final ModelMapper modelMapper;
    private final UserService userService;

    public Chat toEntity(ChatDto chatDto){
        Chat chat = modelMapper.map(chatDto, Chat.class);
        chat.setFirstUser(userService.findOne(chatDto.getSender()));
        chat.setSecondUser(userService.findOne(chatDto.getRecipient()));
        return chat;
    }

    public ChatDto toDto(Chat chat) {
        Long firstUserId = chat.getFirstUser().getId();
        Long secondUserId = chat.getSecondUser().getId();
        ChatDto chatDto = modelMapper.map(chat, ChatDto.class);
        chatDto.setSender(firstUserId);
        chatDto.setRecipient(secondUserId);
        return chatDto;
    }

    public void updateEntityFromDto(ChatDto chatDto, Chat chat) {
        if (chatDto.getSender() != null) {
            chat.setFirstUser(userService.findOne(chatDto.getSender()));
        }
        if (chatDto.getRecipient() != null) {
            chat.setSecondUser(userService.findOne(chatDto.getRecipient()));
        }
        chat.setChanged(Timestamp.valueOf(LocalDateTime.now()));

    }
}
