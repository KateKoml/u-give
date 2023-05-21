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
@Schema(description = "Role Request with role name")
public class RoleRequest {

    @Size(max = 100)
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "ADMIN",
            type = "number", description = "Role name")
    @NotNull
    private String roleName;
}
