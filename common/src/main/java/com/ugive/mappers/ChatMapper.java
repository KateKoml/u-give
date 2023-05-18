package com.ugive.mappers;

import com.ugive.requests.ChatRequest;
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

    public Chat toEntity(ChatRequest chatRequest){
        Chat chat = modelMapper.map(chatRequest, Chat.class);
        chat.setFirstUser(userService.findOne(chatRequest.getSender()));
        chat.setSecondUser(userService.findOne(chatRequest.getRecipient()));
        return chat;
    }

    public void updateEntityFromRequest(ChatRequest chatRequest, Chat chat) {
        if (chatRequest.getSender() != null) {
            chat.setFirstUser(userService.findOne(chatRequest.getSender()));
        }
        if (chatRequest.getRecipient() != null) {
            chat.setSecondUser(userService.findOne(chatRequest.getRecipient()));
        }
        chat.setChanged(Timestamp.valueOf(LocalDateTime.now()));

    }
}
