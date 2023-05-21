package com.ugive.requests;

import com.ugive.models.enums.Gender;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class UserRequest {
    @Size(min = 2, max = 20)
    @Pattern(message = "User name should be from 2 to 20 characters. Example: Nickolay",
            regexp = "^[A-Z][a-z]*[A-Z]?[a-z]*$")
    private String userName;

    @Size(min = 2, max = 30)
    @Pattern(message = "Surname should be from 2 to 30 characters. Example: Balkonsky",
            regexp = "^[A-Z][a-z]*[A-Z]?[a-z]*$")
    private String surname;

    private Gender gender;

    @Size(max = 12)
    @Pattern(message = "Belarusian phone must have 12 numbers. Example: 375254806767",
            regexp = "^(375|80)(29|25|33|44)(\\d{3})(\\d{2})(\\d{2})$")
    private String phone;

    @Size(min = 7,max = 50)
    @Pattern(message = "Invalid email. Example: info_belstu@.gmail.com",
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}$")
    private String email;

    @Size(min = 3, max = 30)
    @Pattern(message = "Login should be from 3 to 30 characters. Example: NickolayBalkonsky",
            regexp = "^[a-zA-Z0-9_-]{3,20}$")
    private String login;

    @Size(min = 6, max = 20)
    @Pattern(message = "Password should be from 6 to 20 characters, containing numbers, lowercase and uppercase letters. Example: NickolayBalkonsky",
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$")
    private String password;
}
