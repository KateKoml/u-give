package com.ugive.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Message Request with chat, sender and text of message")
public class MessageRequest {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1",
            type = "number", description = "Chat id")
    @NotNull
    private Long privateChat;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1",
            type = "number", description = "First user (sender) id")
    @NotNull
    private Long sender;

    @Size(min = 1, max = 200)
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Hello world!",
            type = "string", description = "Text of message")
    @NotNull
    private String text;
}
