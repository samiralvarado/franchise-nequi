package co.com.nequi.franchise_api.infrastructure.adapters.driven.mongo.mapper;

import co.com.nequi.franchise_api.branch.Branch;
import co.com.nequi.franchise_api.franchise.Franchise;
import co.com.nequi.franchise_api.infrastructure.adapters.driven.mongo.entity.FranchiseEntity;
import co.com.nequi.franchise_api.product.Product;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MongoMapperTest {

    @Test
    void shouldMapEntityToDomain() {
        Product product = Product.builder().id("product-id").name("Soda").stock(10).build();
        Branch branch = Branch.builder().id("branch-id").name("Main Branch").products(List.of(product)).build();
        FranchiseEntity entity = FranchiseEntity.builder()
                .id("franchise-id")
                .name("North Franchise")
                .branches(List.of(branch))
                .build();

        Franchise franchise = MongoMapper.toDomain(entity);

        assertEquals("franchise-id", franchise.getId());
        assertEquals("North Franchise", franchise.getName());
        assertEquals("Main Branch", franchise.getBranches().getFirst().getName());
        assertEquals("Soda", franchise.getBranches().getFirst().getProducts().getFirst().getName());
    }

    @Test
    void shouldMapDomainToEntity() {
        Product product = Product.builder().id("product-id").name("Soda").stock(10).build();
        Branch branch = Branch.builder().id("branch-id").name("Main Branch").products(List.of(product)).build();
        Franchise franchise = Franchise.builder()
                .id("franchise-id")
                .name("North Franchise")
                .branches(List.of(branch))
                .build();

        FranchiseEntity entity = MongoMapper.toEntity(franchise);

        assertEquals("franchise-id", entity.getId());
        assertEquals("North Franchise", entity.getName());
        assertEquals("Main Branch", entity.getBranches().getFirst().getName());
        assertEquals("Soda", entity.getBranches().getFirst().getProducts().getFirst().getName());
    }
}
