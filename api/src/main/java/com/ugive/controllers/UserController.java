package com.ugive.controllers;

import com.ugive.dto.UserRequest;
import com.ugive.models.User;
import com.ugive.services.UserService;
import com.ugive.services.impl.UserServiceImpl;
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
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<User> users = userService.findAll(page, size);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findOne(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Optional<User>> createUser(@Valid @RequestBody UserRequest userRequest) {
        Optional<User> createdUser = userService.create(userRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Optional<User>> updateUser(@PathVariable("id") Long id, @RequestBody UserRequest userRequest) {
        Optional<User> updatedUser = userService.update(id, userRequest);
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.softDelete(id);
        return new ResponseEntity<>("Your account is deleted. You can restore it within 30 days.", HttpStatus.OK);
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Optional<User>> resetAccount(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.resetAccount(id), HttpStatus.OK);
    }

    @PostMapping("/{userId}/set_role")
    public ResponseEntity<User> setUserRole(@PathVariable Long userId, @RequestParam String roleName) {
        Optional<User> user = userService.setUserRole(userId, roleName);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
