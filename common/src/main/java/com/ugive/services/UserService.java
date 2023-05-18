package com.ugive.services;

import com.ugive.requests.UserRequest;
import com.ugive.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> create(UserRequest userDto);

    Optional<User> update(Long id, UserRequest userDto);

    Optional<User> setUserRole(Long userId, String roleName);

    List<User> findAll(int page, int size);

    User findOne(Long id);

    void softDelete(Long id);

    Optional<User> resetAccount(Long id);
}
