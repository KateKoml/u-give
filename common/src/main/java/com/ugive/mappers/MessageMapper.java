package com.ugive.mappers;

import com.ugive.dto.MessageRequest;
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

    public Message toEntity(MessageRequest messageRequest) {
        Message message = modelMapper.map(messageRequest, Message.class);
        message.setChat(chatRepository.findById(messageRequest.getPrivateChat()).orElseThrow(() -> new EntityNotFoundException("Chat not found.")));
        message.setUser(userService.findOne(messageRequest.getSender()));
        return message;
    }

    public void updateEntityFromRequest(MessageRequest messageRequest, Message message) {
        if (messageRequest.getPrivateChat() != null) {
            message.setChat(chatRepository.findById(messageRequest.getPrivateChat()).orElseThrow(() -> new EntityNotFoundException("Chat not found.")));
        }
        if (messageRequest.getSender() != null) {
            message.setUser(userService.findOne(messageRequest.getSender()));
        }
        if (messageRequest.getText() != null) {
            message.setText(messageRequest.getText());
        }
        message.setChanged(Timestamp.valueOf(LocalDateTime.now()));
    }
}
