package co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockRequest {

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    @Schema(description = "Current product stock", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer stock;
}
