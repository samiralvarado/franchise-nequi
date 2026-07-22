package co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BranchRequest {

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Schema(description = "El nombre de la sucursal", example = "107 OLÍMPICA LA 76", required = true)
    private String name;
}
