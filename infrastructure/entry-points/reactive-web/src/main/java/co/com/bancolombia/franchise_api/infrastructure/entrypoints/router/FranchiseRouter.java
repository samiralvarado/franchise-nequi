package co.com.bancolombia.franchise_api.infrastructure.entrypoints.router;

import co.com.bancolombia.franchise_api.infrastructure.entrypoints.handler.FranchiseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Tag(name = "Franchises", description = "Reactive franchise management endpoints")
public class FranchiseRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/franchises",
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "create",
                    operation = @Operation(summary = "Create a franchise", tags = {"Franchises"})),
            @RouterOperation(
                    path = "/franchises/{id}/branches",
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "addBranch",
                    operation = @Operation(
                            summary = "Add a branch to a franchise",
                            tags = {"Franchises"},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Franchise id")})),
            @RouterOperation(
                    path = "/franchises/{franchiseId}/branches/{branchId}/products",
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "addProduct",
                    operation = @Operation(
                    summary = "Add a product to a branch",
                    tags = {"Franchises"},
                    parameters = {
                            @Parameter(in = ParameterIn.PATH, name = "franchiseId", description = "Franchise id"),
                            @Parameter(in = ParameterIn.PATH, name = "branchId", description = "Branch id")
                    })),
            @RouterOperation(
                    path = "/franchises/{franchiseId}/branches/{branchId}/products/{productId}",
                    method = RequestMethod.DELETE,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "deleteProduct",
                    operation = @Operation(
                            summary = "Delete a product from a branch",
                            tags = {"Franchises"},
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "franchiseId", description = "Franchise id"),
                                    @Parameter(in = ParameterIn.PATH, name = "branchId", description = "Branch id"),
                                    @Parameter(in = ParameterIn.PATH, name = "productId", description = "Product id")
                            })),
            @RouterOperation(
                    path = "/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock",
                    method = RequestMethod.PATCH,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "updateStock",
                    operation = @Operation(
                    summary = "Update product stock",
                    tags = {"Franchises"},
                    parameters = {
                            @Parameter(in = ParameterIn.PATH, name = "franchiseId", description = "Franchise id"),
                            @Parameter(in = ParameterIn.PATH, name = "branchId", description = "Branch id"),
                            @Parameter(in = ParameterIn.PATH, name = "productId", description = "Product id")
                    })),
            @RouterOperation(
                    path = "/franchises/{id}/max-stock",
                    method = RequestMethod.GET,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "getMaxStockProducts",
                    operation = @Operation(
                            summary = "Get the product with highest stock for each branch",
                            tags = {"Franchises"},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Franchise id")})),
            @RouterOperation(
                    path = "/franchises/{id}",
                    method = RequestMethod.PUT,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "updateFranchiseName",
                    operation = @Operation(
                            summary = "Update franchise name",
                            tags = {"Franchises"},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Franchise id")})),
            @RouterOperation(
                    path = "/franchises/{franchiseId}/branches/{branchId}",
                    method = RequestMethod.PUT,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "updateBranchName",
                    operation = @Operation(
                            summary = "Update branch name",
                            tags = {"Franchises"},
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "franchiseId", description = "Franchise id"),
                                    @Parameter(in = ParameterIn.PATH, name = "branchId", description = "Branch id")
                            })),
            @RouterOperation(
                    path = "/franchises/{franchiseId}/branches/{branchId}/products/{productId}",
                    method = RequestMethod.PUT,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "updateProductName",
                    operation = @Operation(
                            summary = "Update product name",
                            tags = {"Franchises"},
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "franchiseId", description = "Franchise id"),
                                    @Parameter(in = ParameterIn.PATH, name = "branchId", description = "Branch id"),
                                    @Parameter(in = ParameterIn.PATH, name = "productId", description = "Product id")
                            }))
    })
    public RouterFunction<ServerResponse> route(FranchiseHandler handler) {
        return RouterFunctions.route()
                .POST("/franchises", handler::create)
                .POST("/franchises/{id}/branches", handler::addBranch)
                .POST("/franchises/{franchiseId}/branches/{branchId}/products", handler::addProduct)
                .DELETE("/franchises/{franchiseId}/branches/{branchId}/products/{productId}", handler::deleteProduct)
                .PATCH("/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock", handler::updateStock)
                .GET("/franchises/{id}/max-stock", handler::getMaxStockProducts)
                .PUT("/franchises/{id}", handler::updateFranchiseName)
                .PUT("/franchises/{franchiseId}/branches/{branchId}", handler::updateBranchName)
                .PUT("/franchises/{franchiseId}/branches/{branchId}/products/{productId}", handler::updateProductName)
                .build();
    }
}
