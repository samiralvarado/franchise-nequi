package co.com.bancolombia.franchise_api.infrastructure.entrypoints.mapper;

import co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto.BranchRequest;
import co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto.FranchiseRequest;
import co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto.ProductRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntrypointMapperTest {

    @Test
    void shouldMapFranchiseRequestToDomain() {
        FranchiseRequest request = FranchiseRequest.builder()
                .name("North Franchise")
                .build();

        var franchise = FranchiseMapper.toDomain(request);

        assertEquals("North Franchise", franchise.getName().getValue());
    }

    @Test
    void shouldMapBranchRequestToDomain() {
        BranchRequest request = new BranchRequest();
        request.setName("Main Branch");

        var branch = BranchMapper.toDomain(request);

        assertEquals("Main Branch", branch.getName().getValue());
        assertNotNull(branch.getProducts());
        assertTrue(branch.getProducts().isEmpty());
    }

    @Test
    void shouldMapProductRequestToDomain() {
        ProductRequest request = new ProductRequest();
        request.setName("Soda");
        request.setStock(20);

        var product = ProductMapper.toDomain(request);

        assertEquals("Soda", product.getName().getValue());
        assertEquals(20, product.getStock().getValue());
    }
}