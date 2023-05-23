package com.ugive.mappers;

import com.ugive.models.AuthenticationInfo;
import com.ugive.models.User;
import com.ugive.models.enums.Gender;
import com.ugive.requests.UserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public User toEntity(UserRequest userRequest) {
        User user = modelMapper.map(userRequest, User.class);
        if (user.getGender() == null) {
            user.setGender(Gender.NOT_SELECTED);
        }
        user.setAuthenticationInfo(new AuthenticationInfo(userRequest.getEmail(), userRequest.getLogin(), userRequest.getPassword()));
        return user;
    }

    public void updateEntityFromRequest(UserRequest userRequest, User user) {
        if (userRequest.getUserName() != null) {
            user.setUserName(userRequest.getUserName());
        }
        if (userRequest.getSurname() != null) {
            user.setSurname(userRequest.getSurname());
        }
        if (userRequest.getGender() != null) {
            user.setGender(userRequest.getGender());
        }
        if (userRequest.getPhone() != null) {
            user.setPhone(userRequest.getPhone());
        }
        if (userRequest.getEmail() != null) {
            user.getAuthenticationInfo().setEmail(userRequest.getEmail());
        }
        if (userRequest.getLogin() != null) {
            user.getAuthenticationInfo().setLogin(userRequest.getLogin());
        }
        if (userRequest.getPassword() != null) {
            user.getAuthenticationInfo().setPassword(userRequest.getPassword());
        }
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
    }
}
