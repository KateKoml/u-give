package com.ugive.services;

import com.ugive.dto.UserDto;
import com.ugive.models.User;

import java.util.Optional;

public interface UserService {
    Optional<User> create(UserDto userDto);
    Optional<User> update(Long id, UserDto userDto);
}
