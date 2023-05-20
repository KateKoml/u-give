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
    @Pattern(regexp = "^[A-Z][a-z]*[A-Z]?[a-z]*$")
    private String userName;

    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[A-Z][a-z]*[A-Z]?[a-z]*$")
    private String surname;

    private Gender gender;

    @Size(max = 12)
    @Pattern(regexp = "^(375|80)(29|25|33|44)(\\d{3})(\\d{2})(\\d{2})$")
    private String phone;

    @Size(max = 50)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,}$")
    private String email;

    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,16}$")
    private String login;

    @Size(min = 6, max = 20)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$")
    private String password;
}
