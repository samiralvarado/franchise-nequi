package co.com.bancolombia.usecase;

import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.dto.ProductReport;
import co.com.bancolombia.model.product.values.ProductName;
import co.com.bancolombia.model.product.values.ProductStock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMaxStockUseCaseTest {

    @Mock
    private FranchiseRepository repository;

    @InjectMocks
    private GetMaxStockUseCase useCase;

    @Test
    void shouldReturnMaxStockProductsByBranch() {
        ProductReport report = ProductReport.builder()
                .branchName("Main Branch")
                .product(Product.builder().id("product-id").name(ProductName.of("Soda")).stock(ProductStock.of(50)).build())
                .build();

        when(repository.getMaxStockPerBranch("franchise-id")).thenReturn(Flux.just(report));

        StepVerifier.create(useCase.execute("franchise-id"))
                .expectNextMatches(result -> result.getBranchName().equals("Main Branch")
                        && result.getProduct().getStock().getValue() == 50)
                .verifyComplete();
    }
}