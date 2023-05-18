package com.ugive.dto;

import com.ugive.models.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String userName;
    private String surname;
    private Gender gender;
    private String phone;
    private String email;
    private String login;
    private String password;
}
