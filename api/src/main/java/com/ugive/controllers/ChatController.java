package com.ugive.controllers;

import com.ugive.requests.ChatRequest;
import com.ugive.models.Chat;
import com.ugive.services.ChatService;
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
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/chats")
    public ResponseEntity<List<Chat>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Chat> chats = chatService.findAll(page, size);
        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    @GetMapping("/chats/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long id) {
        Chat chat = chatService.findOne(id);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping("/{userId}/chats")
    public ResponseEntity<List<Chat>> getAllUserChats(@PathVariable Long userId) {
        List<Chat> userChats = chatService.getListOfChatsForUser(userId);
        return new ResponseEntity<>(userChats, HttpStatus.OK);
    }

    @PostMapping("/chats")
    public ResponseEntity<Optional<Chat>> createChat(@Valid @RequestBody ChatRequest chatRequest) {
        Optional<Chat> chat = chatService.create(chatRequest);
        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }

    @PutMapping("/chats/{id}/update")
    public ResponseEntity<Optional<Chat>> updateChat(@PathVariable("id") Long id, @RequestBody ChatRequest chatRequest) {
        Optional<Chat> chat = chatService.update(id, chatRequest);
        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }

    @DeleteMapping("/chats/{id}")
    public ResponseEntity<String> deleteChat(@PathVariable("id") Long id) {
        chatService.softDelete(id);
        return new ResponseEntity<>("This chat is deleted.", HttpStatus.OK);
    }
}
