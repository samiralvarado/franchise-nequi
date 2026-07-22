package co.com.bancolombia.usecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.values.BranchName;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.InvalidProductException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.values.ProductName;
import co.com.bancolombia.model.product.values.ProductStock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateStockUseCaseTest {

    @Mock
    private FranchiseRepository repository;

    @InjectMocks
    private UpdateStockUseCase useCase;

    @Test
    void shouldUpdateProductStock() {
        Product product = Product.builder().id("product-id").name(ProductName.of("Soda")).stock(ProductStock.of(10)).build();
        Branch branch = Branch.builder().id("branch-id").name(BranchName.of("Main Branch")).products(new ArrayList<>(List.of(product))).build();
        Franchise franchise = Franchise.builder().id("franchise-id").branches(List.of(branch)).build();

        when(repository.findById("franchise-id")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.updateStock("franchise-id", "branch-id", "product-id", 25))
                .expectNextMatches(saved -> saved.getBranches().getFirst().getProducts().getFirst().getStock().getValue() == 25)
                .verifyComplete();
    }

    @Test
    void shouldFailWhenStockIsNegative() {
        StepVerifier.create(useCase.updateStock("franchise-id", "branch-id", "product-id", -1))
                .expectError(InvalidProductException.class)
                .verify();
    }
}