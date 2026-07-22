package co.com.nequi.franchise_api.infrastructure.adapters.driven.mongo.mapper;

import co.com.bancolombia.franchise_api.infrastructure.adapters.driven.mongo.entity.FranchiseEntity;
import co.com.bancolombia.franchise_api.infrastructure.adapters.driven.mongo.mapper.MongoMapper;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.values.BranchName;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.values.FranchiseName;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.values.ProductName;
import co.com.bancolombia.model.product.values.ProductStock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MongoMapperTest {

    @Test
    void shouldMapEntityToDomain() {
        Product product = Product.builder().id("product-id").name(ProductName.of("Soda")).stock(ProductStock.of(10)).build();
        Branch branch = Branch.builder().id("branch-id").name(BranchName.of("Main Branch")).products(List.of(product)).build();
        FranchiseEntity entity = FranchiseEntity.builder()
                .id("franchise-id")
                .name("North Franchise")
                .branches(List.of(branch))
                .build();

        Franchise franchise = MongoMapper.toDomain(entity);

        assertEquals("franchise-id", franchise.getId());
        assertEquals("North Franchise", franchise.getName().getValue());
        assertEquals("Main Branch", franchise.getBranches().getFirst().getName().getValue());
        assertEquals("Soda", franchise.getBranches().getFirst().getProducts().getFirst().getName().getValue());
    }

    @Test
    void shouldMapDomainToEntity() {
        Product product = Product.builder().id("product-id").name(ProductName.of("Soda")).stock(ProductStock.of(10)).build();
        Branch branch = Branch.builder().id("branch-id").name(BranchName.of("Main Branch")).products(List.of(product)).build();
        Franchise franchise = Franchise.builder()
                .id("franchise-id")
                .name(FranchiseName.of("North Franchise"))
                .branches(List.of(branch))
                .build();

        FranchiseEntity entity = MongoMapper.toEntity(franchise);

        assertEquals("franchise-id", entity.getId());
        assertEquals("North Franchise", entity.getName());
        assertEquals("Main Branch", entity.getBranches().getFirst().getName());
        assertEquals("Soda", entity.getBranches().getFirst().getProducts().getFirst().getName());
    }
}