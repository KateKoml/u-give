package com.ugive.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Chat Request between two users")
public class ChatRequest {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1",
            type = "number", description = "First user (sender) id")
    @NotNull
    private Long sender;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "2",
            type = "number", description = "Second user (sender) id")
    @NotNull
    private Long recipient;
}
