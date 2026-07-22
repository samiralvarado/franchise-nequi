package co.com.nequi.franchise_api.infrastructure.adapters.driven.mongo.repository;

import co.com.nequi.franchise_api.branch.Branch;
import co.com.nequi.franchise_api.franchise.Franchise;
import co.com.nequi.franchise_api.infrastructure.adapters.driven.mongo.entity.FranchiseEntity;
import co.com.nequi.franchise_api.product.Product;
import co.com.nequi.franchise_api.product.dto.ProductReport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseRepositoryImplTest {

    @Mock
    private MongoReactiveRepository mongoReactiveRepository;

    @Mock
    private ReactiveMongoTemplate mongoTemplate;

    @InjectMocks
    private FranchiseRepositoryImpl repository;

    @Test
    void shouldSaveFranchise() {
        Product product = Product.builder().id("product-id").name("Soda").stock(10).build();
        Branch branch = Branch.builder().id("branch-id").name("Main Branch").products(List.of(product)).build();
        Franchise franchise = Franchise.builder()
                .id("franchise-id")
                .name("North Franchise")
                .branches(List.of(branch))
                .build();
        FranchiseEntity entity = FranchiseEntity.builder()
                .id("franchise-id")
                .name("North Franchise")
                .branches(List.of(branch))
                .build();

        when(mongoReactiveRepository.save(any(FranchiseEntity.class))).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.save(franchise))
                .expectNextMatches(saved -> saved.getId().equals("franchise-id")
                        && saved.getBranches().getFirst().getProducts().getFirst().getName().equals("Soda"))
                .verifyComplete();

        verify(mongoReactiveRepository).save(any(FranchiseEntity.class));
    }

    @Test
    void shouldFindById() {
        FranchiseEntity entity = FranchiseEntity.builder()
                .id("franchise-id")
                .name("North Franchise")
                .build();

        when(mongoReactiveRepository.findById("franchise-id")).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.findById("franchise-id"))
                .expectNextMatches(franchise -> franchise.getName().equals("North Franchise"))
                .verifyComplete();
    }

    @Test
    void shouldFindByName() {
        FranchiseEntity entity = FranchiseEntity.builder()
                .id("franchise-id")
                .name("North Franchise")
                .build();

        when(mongoReactiveRepository.findByName("North Franchise")).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.findByName("North Franchise"))
                .expectNextMatches(franchise -> franchise.getId().equals("franchise-id"))
                .verifyComplete();
    }

    @Test
    void shouldGetMaxStockPerBranch() {
        ProductReport report = ProductReport.builder()
                .branchName("Main Branch")
                .product(Product.builder().id("product-id").name("Soda").stock(50).build())
                .build();

        when(mongoTemplate.aggregate(any(Aggregation.class), eq("franchises"), eq(ProductReport.class)))
                .thenReturn(Flux.just(report));

        StepVerifier.create(repository.getMaxStockPerBranch("franchise-id"))
                .expectNextMatches(result -> result.getBranchName().equals("Main Branch")
                        && result.getProduct().getStock() == 50)
                .verifyComplete();
    }
}
