package com.ugive.services;

import com.ugive.dto.UserDto;
import com.ugive.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> create(UserDto userDto);
    Optional<User> update(Long id, UserDto userDto);
    public Optional<User> setUserRole(Long userId, String roleName);
    public List<User> findAll(int page, int size);
    public User findOne(Long id);
    public void softDelete(Long id);
    public Optional<User> resetAccount(Long id);
}
