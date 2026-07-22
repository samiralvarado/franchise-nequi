package co.com.bancolombia.usecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.BranchNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.FranchiseNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.InvalidProductException;
import co.com.bancolombia.model.franchise.exceptions.ProductNotFoundException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.values.ProductStock;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UpdateStockUseCase {

    private final FranchiseRepository repository;

    public Mono<Franchise> updateStock(String franchiseId, String branchId, String productId, Integer newStock) {
        return Mono.justOrEmpty(newStock)
                .switchIfEmpty(Mono.error(new InvalidProductException("Product stock must be zero or greater")))
                .flatMap(stock -> Mono.fromCallable(() -> ProductStock.of(stock)))
                .onErrorMap(IllegalArgumentException.class, ex -> new InvalidProductException(ex.getMessage()))
                .flatMap(validProductStock -> repository.findById(franchiseId)
                        .switchIfEmpty(Mono.error(new FranchiseNotFoundException()))
                        .map(franchise -> {
                            List<Branch> branches = franchise.getBranches() != null
                                    ? franchise.getBranches()
                                    : new ArrayList<>();

                            Branch targetBranch = branches.stream()
                                    .filter(b -> b.getId().equals(branchId))
                                    .findFirst()
                                    .orElseThrow(BranchNotFoundException::new);

                            List<Product> products = targetBranch.getProducts() != null
                                    ? targetBranch.getProducts()
                                    : new ArrayList<>();

                            Product targetProduct = products.stream()
                                    .filter(p -> p.getId().equals(productId))
                                    .findFirst()
                                    .orElseThrow(ProductNotFoundException::new);

                            Product updatedProduct = targetProduct.toBuilder()
                                    .stock(validProductStock)
                                    .build();

                            List<Product> updatedProducts = products.stream()
                                    .map(p -> p.getId().equals(productId) ? updatedProduct : p)
                                    .toList();

                            Branch updatedBranch = targetBranch.toBuilder()
                                    .products(updatedProducts)
                                    .build();

                            List<Branch> updatedBranches = branches.stream()
                                    .map(b -> b.getId().equals(branchId) ? updatedBranch : b)
                                    .toList();

                            return franchise.toBuilder()
                                    .branches(updatedBranches)
                                    .build();
                        })
                )
                .flatMap(repository::save);
    }
}
