package com.ugive.requests;

import com.ugive.models.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "User Request with all needed information")
public class UserRequest {
    @Size(min = 2, max = 20)
    @Pattern(message = "User name should be from 2 to 20 characters.",
            regexp = "^[A-Z][a-z]*[A-Z]?[a-z]*$")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Kate",
            type = "string", description = "User name")
    @NotNull
    private String userName;

    @Size(min = 2, max = 30)
    @Pattern(message = "Surname should be from 2 to 30 characters.",
            regexp = "^[A-Z][a-z]*[A-Z]?[a-z]*$")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Komleva",
            type = "string", description = "User surname")
    @NotNull
    private String surname;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "FEMALE",
            type = "Gender", description = "User gender")
    private Gender gender;

    @Size(max = 12)
    @Pattern(message = "Belarusian phone must have 12 numbers.",
            regexp = "^(375|80)(29|25|33|44)(\\d{3})(\\d{2})(\\d{2})$")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "375295905041",
            type = "string", description = "Phone number")
    @NotNull
    private String phone;

    @Size(min = 7, max = 50)
    @Email(message = "Invalid email.")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "komlevakate99@gmail.com",
            type = "string", description = "User email")
    @NotNull
    private String email;

    @Size(min = 3, max = 30)
    @Pattern(message = "Login should be from 3 to 30 characters.",
            regexp = "^[a-zA-Z0-9_-]{3,20}$")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "KateKomleva99",
            type = "string", description = "User login")
    @NotNull
    private String login;

    @Size(min = 6, max = 20)
    @Pattern(message = "Password should be from 6 to 20 characters, containing numbers, lowercase and uppercase letters.",
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "fyjhhk57FHg",
            type = "string", description = "User password")
    @NotNull
    private String password;
}
