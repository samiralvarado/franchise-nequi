package co.com.bancolombia.franchise_api.infrastructure.entrypoints.handler;

import co.com.bancolombia.franchise_api.branch.Branch;
import co.com.bancolombia.franchise_api.domain.usecase.AddBranchUseCase;
import co.com.bancolombia.franchise_api.domain.usecase.AddProductUseCase;
import co.com.bancolombia.franchise_api.domain.usecase.CreateFranchiseUseCase;
import co.com.bancolombia.franchise_api.domain.usecase.DeleteProductUseCase;
import co.com.bancolombia.franchise_api.domain.usecase.GetMaxStockUseCase;
import co.com.bancolombia.franchise_api.domain.usecase.UpdateBranchNameUseCase;
import co.com.bancolombia.franchise_api.domain.usecase.UpdateFranchiseUseCase;
import co.com.bancolombia.franchise_api.domain.usecase.UpdateProductNameUseCase;
import co.com.bancolombia.franchise_api.domain.usecase.UpdateStockUseCase;
import co.com.bancolombia.franchise_api.franchise.Franchise;
import co.com.bancolombia.franchise_api.franchise.exceptions.FranchiseAlreadyExistsException;
import co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto.FranchiseRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FranchiseHandlerTest {

    private CreateFranchiseUseCase createFranchiseUseCase;
    private AddBranchUseCase addBranchUseCase;
    private FranchiseHandler handler;

    @BeforeEach
    void setUp() {
        createFranchiseUseCase = mock(CreateFranchiseUseCase.class);
        addBranchUseCase = mock(AddBranchUseCase.class);
        AddProductUseCase addProductUseCase = mock(AddProductUseCase.class);
        DeleteProductUseCase deleteProductUseCase = mock(DeleteProductUseCase.class);
        UpdateStockUseCase updateStockUseCase = mock(UpdateStockUseCase.class);
        GetMaxStockUseCase getMaxStockUseCase = mock(GetMaxStockUseCase.class);
        UpdateFranchiseUseCase updateFranchiseUseCase = mock(UpdateFranchiseUseCase.class);
        UpdateProductNameUseCase updateProductNameUseCase = mock(UpdateProductNameUseCase.class);
        UpdateBranchNameUseCase updateBranchNameUseCase = mock(UpdateBranchNameUseCase.class);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        handler = new FranchiseHandler(
                createFranchiseUseCase,
                addBranchUseCase,
                addProductUseCase,
                deleteProductUseCase,
                updateStockUseCase,
                getMaxStockUseCase,
                updateFranchiseUseCase,
                updateProductNameUseCase,
                updateBranchNameUseCase,
                validator
        );
    }

    @Test
    void shouldCreateFranchise() {
        FranchiseRequest requestBody = FranchiseRequest.builder()
                .name("North Franchise")
                .build();
        Franchise franchise = Franchise.builder()
                .id("franchise-id")
                .name("North Franchise")
                .branches(new ArrayList<>())
                .build();

        when(createFranchiseUseCase.execute(any(Franchise.class))).thenReturn(Mono.just(franchise));

        var request = MockServerRequest.builder().body(Mono.just(requestBody));

        StepVerifier.create(handler.create(request))
                .assertNext(response -> assertEquals(HttpStatus.CREATED, response.statusCode()))
                .verifyComplete();
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        FranchiseRequest requestBody = FranchiseRequest.builder()
                .name("")
                .build();

        var request = MockServerRequest.builder().body(Mono.just(requestBody));

        StepVerifier.create(handler.create(request))
                .assertNext(response -> assertEquals(HttpStatus.BAD_REQUEST, response.statusCode()))
                .verifyComplete();
    }

    @Test
    void shouldReturnBadRequestWhenUseCaseFails() {
        FranchiseRequest requestBody = FranchiseRequest.builder()
                .name("North Franchise")
                .build();

        when(createFranchiseUseCase.execute(any(Franchise.class)))
                .thenReturn(Mono.error(new FranchiseAlreadyExistsException()));

        var request = MockServerRequest.builder().body(Mono.just(requestBody));

        StepVerifier.create(handler.create(request))
                .assertNext(response -> assertEquals(HttpStatus.BAD_REQUEST, response.statusCode()))
                .verifyComplete();
    }

    @Test
    void shouldAddBranch() {
        Franchise franchise = Franchise.builder()
                .id("franchise-id")
                .branches(new ArrayList<>())
                .build();

        when(addBranchUseCase.execute(any(String.class), any(Branch.class))).thenReturn(Mono.just(franchise));

        var request = MockServerRequest.builder()
                .pathVariable("id", "franchise-id")
                .body(Mono.just(branchRequest("Main Branch")));

        StepVerifier.create(handler.addBranch(request))
                .assertNext(response -> assertEquals(HttpStatus.OK, response.statusCode()))
                .verifyComplete();
    }

    private co.com.nequi.franchise_api.infrastructure.entrypoints.dto.BranchRequest branchRequest(String name) {
        var request = new co.com.nequi.franchise_api.infrastructure.entrypoints.dto.BranchRequest();
        request.setName(name);
        return request;
    }

    private void assertEquals(Object expected, Object actual) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
    }
}
