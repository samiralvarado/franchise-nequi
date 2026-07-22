package co.com.bancolombia.franchise_api.infrastructure.entrypoints.handler;


import co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto.BranchRequest;
import co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto.FranchiseRequest;
import co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto.NameRequest;
import co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto.ProductRequest;
import co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto.StockRequest;
import co.com.bancolombia.franchise_api.infrastructure.entrypoints.mapper.BranchMapper;
import co.com.bancolombia.franchise_api.infrastructure.entrypoints.mapper.FranchiseMapper;
import co.com.bancolombia.franchise_api.infrastructure.entrypoints.mapper.ProductMapper;
import co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto.ErrorResponse;
import co.com.bancolombia.model.product.dto.ProductReport;
import co.com.bancolombia.model.franchise.exceptions.DomainException;
import co.com.bancolombia.model.franchise.exceptions.RequestValidationException;
import co.com.bancolombia.usecase.AddBranchUseCase;
import co.com.bancolombia.usecase.AddProductUseCase;
import co.com.bancolombia.usecase.CreateFranchiseUseCase;
import co.com.bancolombia.usecase.DeleteProductUseCase;
import co.com.bancolombia.usecase.GetMaxStockUseCase;
import co.com.bancolombia.usecase.UpdateBranchNameUseCase;
import co.com.bancolombia.usecase.UpdateFranchiseUseCase;
import co.com.bancolombia.usecase.UpdateProductNameUseCase;
import co.com.bancolombia.usecase.UpdateStockUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final AddBranchUseCase addBranchUseCase;
    private final AddProductUseCase addProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UpdateStockUseCase updateStockUseCase;
    private final GetMaxStockUseCase getMaxStockUseCase;
    private final UpdateFranchiseUseCase updateFranchiseUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;
    private final Validator validator;

    @Operation(
            summary = "Create a franchise",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = FranchiseRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Franchise created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(FranchiseRequest.class)
                .flatMap(this::validate)
                .flatMap(dto -> createFranchiseUseCase.execute(FranchiseMapper.toDomain(dto)))
                .flatMap(franchise -> ServerResponse.status(201).bodyValue(franchise))
                .onErrorResume(this::handleError);
    }

    @Operation(
            summary = "Add a branch to an existing franchise",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = BranchRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Branch added"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public Mono<ServerResponse> addBranch(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(BranchRequest.class)
                .flatMap(this::validate)
                .map(BranchMapper::toDomain)
                .flatMap(branch -> addBranchUseCase.execute(id, branch))
                .flatMap(franchise -> ServerResponse.ok().bodyValue(franchise))
                .onErrorResume(this::handleError);
    }

    @Operation(
            summary = "Add a product to a branch",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product added"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public Mono<ServerResponse> addProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");

        return request.bodyToMono(ProductRequest.class)
                .flatMap(this::validate)
                .map(ProductMapper::toDomain)
                .flatMap(product -> addProductUseCase.execute(franchiseId, branchId, product))
                .flatMap(franchise -> ServerResponse.ok().bodyValue(franchise))
                .onErrorResume(this::handleError);
    }


    @Operation(
            summary = "Delete a product from a branch",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");

        return deleteProductUseCase.execute(franchiseId, branchId, productId)
                .flatMap(franchise -> ServerResponse.ok().build())
                .onErrorResume(this::handleError);
    }

    @Operation(
            summary = "Update product stock",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = StockRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stock updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public Mono<ServerResponse> updateStock(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");

        return request.bodyToMono(StockRequest.class)
                .flatMap(this::validate)
                .flatMap(body -> updateStockUseCase.updateStock(franchiseId, branchId, productId, body.getStock()))
                .flatMap(franchise -> ServerResponse.ok().bodyValue(franchise))
                .onErrorResume(this::handleError);
    }

    @Operation(
            summary = "Get the product with highest stock for each branch",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products returned"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public Mono<ServerResponse> getMaxStockProducts(ServerRequest request) {
        String id = request.pathVariable("id");

        return ServerResponse.ok()
                .body(getMaxStockUseCase.execute(id), ProductReport.class)
                .onErrorResume(this::handleError);
    }

    @Operation(
            summary = "Update franchise name",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = NameRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Franchise updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public Mono<ServerResponse> updateFranchiseName(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(NameRequest.class)
                .flatMap(this::validate)
                .flatMap(body -> updateFranchiseUseCase.execute(id, body.getName()))
                .flatMap(f -> ServerResponse.ok().bodyValue(f))
                .onErrorResume(this::handleError);
    }

    @Operation(
            summary = "Update branch name",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = NameRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Branch updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public Mono<ServerResponse> updateBranchName(ServerRequest request) {
        String fId = request.pathVariable("franchiseId");
        String bId = request.pathVariable("branchId");
        return request.bodyToMono(NameRequest.class)
                .flatMap(this::validate)
                .flatMap(body -> updateBranchNameUseCase.updateBranchName(fId, bId, body.getName()))
                .flatMap(f -> ServerResponse.ok().bodyValue(f))
                .onErrorResume(this::handleError);
    }

    @Operation(
            summary = "Update product name",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = NameRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public Mono<ServerResponse> updateProductName(ServerRequest request) {
        String fId = request.pathVariable("franchiseId");
        String bId = request.pathVariable("branchId");
        String pId = request.pathVariable("productId");
        return request.bodyToMono(NameRequest.class)
                .flatMap(this::validate)
                .flatMap(body -> updateProductNameUseCase.updateProductName(fId, bId, pId, body.getName()))
                .flatMap(f -> ServerResponse.ok().bodyValue(f))
                .onErrorResume(this::handleError);
    }

    private <T> Mono<T> validate(T body) {
        var violations = validator.validate(body);
        if (violations.isEmpty()) {
            return Mono.just(body);
        }
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<T> violation : violations) {
            if (!message.isEmpty()) {
                message.append(", ");
            }
            message.append(violation.getMessage());
        }
        return Mono.error(new RequestValidationException(message.toString()));
    }

    private Mono<ServerResponse> handleError(Throwable error) {
        HttpStatus status = error instanceof DomainException ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
        String message = error instanceof DomainException ? error.getMessage() : "Unexpected error processing request";
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();
        return ServerResponse.status(status).bodyValue(response);
    }
}
