package co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Schema(
            description = "Nombre descriptivo del producto",
            example = "Coca-Cola 600ml",
            required = true
    )
    private String name;

    @Min(value = 0, message = "Stock cannot be negative")
    @Schema(
            description = "Cantidad actual de unidades en inventario",
            example = "50",
            required = true
    )
    private Integer stock;

}