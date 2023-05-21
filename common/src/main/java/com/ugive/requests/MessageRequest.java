package com.ugive.requests;

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
public class MessageRequest {
    @NotNull
    private Long privateChat;
    @NotNull
    private Long sender;
    @Size(min = 1, max = 200)
    @NotNull
    private String text;
}
