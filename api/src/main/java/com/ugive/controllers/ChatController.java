package com.ugive.controllers;

import com.ugive.models.Chat;
import com.ugive.requests.ChatRequest;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<Chat>> findAll() {
        List<Chat> chats = chatService.findAll();
        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long id) {
        Chat chat = chatService.findOne(id);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Chat>> getAllUserChats(@PathVariable Long userId) {
        List<Chat> userChats = chatService.getListOfChatsForUser(userId);
        return new ResponseEntity<>(userChats, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(@Valid @RequestBody ChatRequest chatRequest) {
        Chat chat = chatService.create(chatRequest);
        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chat> updateChat(@PathVariable("id") Long id, @RequestBody ChatRequest chatRequest) {
        Chat chat = chatService.update(id, chatRequest);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChat(@PathVariable("id") Long id) {
        chatService.softDelete(id);
        return new ResponseEntity<>("This chat is deleted.", HttpStatus.OK);
    }
}
