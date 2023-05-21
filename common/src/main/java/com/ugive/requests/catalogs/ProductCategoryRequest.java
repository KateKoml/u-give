package com.ugive.requests.catalogs;

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
@Schema(description = "Product Category Request with name")
public class ProductCategoryRequest {
    @Size(min = 3, max = 100)
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "For adult",
            type = "string", description = "Category name")
    @NotNull
    private String categoryName;
}
