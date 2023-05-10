package com.ugive.mappers;

import com.ugive.dto.UserDto;
import com.ugive.models.AuthenticationInfo;
import com.ugive.models.User;
import com.ugive.models.enums.Gender;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public User toEntity(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        if (user.getGender() == null) {
            user.setGender(Gender.NOT_SELECTED);
        }
        user.setAuthenticationInfo(new AuthenticationInfo(userDto.getEmail(), userDto.getLogin(), userDto.getPassword()));
        return user;
    }

    public UserDto toDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setEmail(user.getAuthenticationInfo().getEmail());
        userDto.setLogin(user.getAuthenticationInfo().getLogin());
        userDto.setPassword(user.getAuthenticationInfo().getPassword());
        return userDto;
    }

    public void updateEntityFromDto(UserDto userDTO, User user) {
        if (userDTO.getUserName() != null) {
            user.setUserName(userDTO.getUserName());
        }
        if (userDTO.getSurname() != null) {
            user.setSurname(userDTO.getSurname());
        }
        if (userDTO.getGender() != null) {
            user.setGender(userDTO.getGender());
        }
        if (userDTO.getPhone() != null) {
            user.setPhone(userDTO.getPhone());
        }
        if (userDTO.getEmail() != null) {
            user.getAuthenticationInfo().setEmail(userDTO.getEmail());
        }
        if (userDTO.getLogin() != null) {
            user.getAuthenticationInfo().setLogin(userDTO.getLogin());
        }
        if (userDTO.getPassword() != null) {
            user.getAuthenticationInfo().setPassword(userDTO.getPassword());
        }
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
    }
}
