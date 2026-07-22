package co.com.bancolombia.usecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.values.BranchName;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.ProductNotFoundException;
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
class DeleteProductUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private DeleteProductUseCase useCase;

    @Test
    void shouldDeleteProduct() {
        Product product = Product.builder().id("product-id").name(ProductName.of("Soda")).stock(ProductStock.of(10)).build();
        Branch branch = Branch.builder().id("branch-id").name(BranchName.of("Main Branch")).products(new ArrayList<>(List.of(product))).build();
        Franchise franchise = Franchise.builder().id("franchise-id").branches(List.of(branch)).build();

        when(franchiseRepository.findById("franchise-id")).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.execute("franchise-id", "branch-id", "product-id"))
                .expectNextMatches(saved -> saved.getBranches().getFirst().getProducts().isEmpty())
                .verifyComplete();
    }

    @Test
    void shouldFailWhenProductDoesNotExist() {
        Branch branch = Branch.builder().id("branch-id").name(BranchName.of("Main Branch")).products(new ArrayList<>()).build();
        Franchise franchise = Franchise.builder().id("franchise-id").branches(List.of(branch)).build();

        when(franchiseRepository.findById("franchise-id")).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.execute("franchise-id", "branch-id", "missing-id"))
                .expectError(ProductNotFoundException.class)
                .verify();
    }
}