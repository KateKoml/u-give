package com.ugive.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthRequest {

    @Schema(example = "komlevakate99@gmail.com", type = "string", description = "User Login")
    private String login;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "kate666", type = "string", description = "User password")
    private String userPassword;
}