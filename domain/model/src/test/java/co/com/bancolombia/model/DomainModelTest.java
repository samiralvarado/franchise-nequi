package co.com.bancolombia.model;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.values.BranchName;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.BranchNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.FranchiseException;
import co.com.bancolombia.model.franchise.exceptions.ProductNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.RequestValidationException;
import co.com.bancolombia.model.franchise.values.FranchiseName;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.dto.ProductReport;
import co.com.bancolombia.model.product.values.ProductName;
import co.com.bancolombia.model.product.values.ProductStock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DomainModelTest {

    @Test
    void shouldBuildFranchiseWithDefaultBranches() {
        Franchise franchise = Franchise.builder()
                .id("franchise-id")
                .name(FranchiseName.of("North Franchise"))
                .build();

        assertEquals("franchise-id", franchise.getId());
        assertEquals("North Franchise", franchise.getName().getValue());
        assertNotNull(franchise.getBranches());
        assertTrue(franchise.getBranches().isEmpty());
    }

    @Test
    void shouldBuildBranchWithProducts() {
        Product product = Product.builder()
                .id("product-id")
                .name(ProductName.of("Soda"))
                .stock(ProductStock.of(10))
                .build();

        Branch branch = Branch.builder()
                .id("branch-id")
                .name(BranchName.of("Main Branch"))
                .products(List.of(product))
                .build();

        assertEquals("branch-id", branch.getId());
        assertEquals("Main Branch", branch.getName().getValue());
        assertEquals("Soda", branch.getProducts().getFirst().getName().getValue());
    }

    @Test
    void shouldBuildProductReport() {
        Product product = Product.builder()
                .id("product-id")
                .name(ProductName.of("Soda"))
                .stock(ProductStock.of(50))
                .build();

        ProductReport report = ProductReport.builder()
                .branchName("Main Branch")
                .product(product)
                .build();

        assertEquals("Main Branch", report.getBranchName());
        assertEquals(50, report.getProduct().getStock().getValue());
    }

    @Test
    void shouldCreateValueObjects() {
        assertEquals("North Franchise", FranchiseName.of("North Franchise").getValue());
        assertEquals("Main Branch", BranchName.of("Main Branch").getValue());
        assertEquals("Soda", ProductName.of("Soda").getValue());
        assertEquals(10, ProductStock.of(10).getValue());
    }

    @Test
    void shouldExposeDomainExceptionMessages() {
        assertEquals("Branch not found", new BranchNotFoundException().getMessage());
        assertEquals("Product not found", new ProductNotFoundException().getMessage());
        assertEquals("Invalid request", new RequestValidationException("Invalid request").getMessage());
        assertEquals("Franchise error", new FranchiseException("Franchise error").getMessage());
    }
}