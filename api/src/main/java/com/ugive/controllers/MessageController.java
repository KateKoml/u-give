package com.ugive.controllers;

import com.ugive.requests.MessageRequest;
import com.ugive.models.Message;
import com.ugive.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/chats/messages")
    public ResponseEntity<Optional<Message>> createMessage(@Valid @RequestBody MessageRequest messageDto) {
        Optional<Message> message = messageService.create(messageDto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PutMapping("/chats/messages/{id}/update")
    public ResponseEntity<Optional<Message>> updateMessage(@PathVariable("id") Long id, @RequestBody MessageRequest messageDto) {
        Optional<Message> message = messageService.update(id, messageDto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/chats/messages/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Message message = messageService.findOne(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/{userId}/chats/messages")
    public ResponseEntity<List<Message>> getAllUserMessages(@PathVariable Long userId) {
        List<Message> messages = messageService.findAllForOneUser(userId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/chats/{chatId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesInChat(@PathVariable Long chatId) {
        List<Message> messages = messageService.showAllMessagesInChat(chatId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/chats/{chatId}/messages/search")
    public ResponseEntity<List<Message>> getAllMessagesInChatByText(@PathVariable Long chatId, @RequestParam String text) {
        List<Message> messages = messageService.searchMessagesInChat(chatId, text);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PutMapping("/chats/messages/{id}/read")
    public ResponseEntity<Message> markMessageAsRead(@PathVariable Long id) {
        Message message = messageService.markAsRead(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/chats/messages/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable("id") Long id) {
        messageService.softDelete(id);
        return new ResponseEntity<>("Message is deleted.", HttpStatus.OK);
    }
}
