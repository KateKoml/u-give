package com.ugive.controllers;

import com.ugive.dto.MessageDto;
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

    @GetMapping("/chats/messages")
    public ResponseEntity<List<MessageDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<MessageDto> messages = messageService.findAll(page, size);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/chats/messages/{id}")
    public ResponseEntity<MessageDto> getMessageById(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.findOne(id));
    }

    @GetMapping("/{userId}/chats/messages")
    public ResponseEntity<List<MessageDto>> getAllUserMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.findAllForOneUser(userId));
    }

    @GetMapping("/chats/{chatId}/messages")
    public ResponseEntity<List<MessageDto>> getAllMessagesInChat(@PathVariable Long chatId) {
        return ResponseEntity.ok(messageService.showAllMessagesInChat(chatId));
    }

    @GetMapping("/chats/{chatId}/messages/search")
    public ResponseEntity<List<MessageDto>> getAllMessagesInChatByText(@PathVariable Long chatId, @RequestParam String text) {
        return ResponseEntity.ok(messageService.searchMessagesInChat(chatId, text));
    }

    @PutMapping("/chats/messages/{id}/read")
    public ResponseEntity<Message> markMessageAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.markAsRead(id));
    }

    @PostMapping("/chats/messages/create")
    public ResponseEntity<Optional<Message>> createMessage(@Valid @RequestBody MessageDto messageDto) {
        Optional<Message> message = messageService.create(messageDto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PutMapping("/chats/messages/{id}/update")
    public ResponseEntity<Optional<Message>> updateMessage(@PathVariable("id") Long id, @RequestBody MessageDto messageDto) {
        Optional<Message> message = messageService.update(id, messageDto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @DeleteMapping("/chats/messages/{id}/delete")
    public ResponseEntity<String> deleteMessage(@PathVariable("id") Long id) {
        messageService.softDelete(id);
        return new ResponseEntity<>("Message is deleted.", HttpStatus.OK);
    }
}
