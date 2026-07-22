package co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NameRequest {

    @NotBlank(message = "Name is required")
    @Schema(description = "New resource name", example = "Updated name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
