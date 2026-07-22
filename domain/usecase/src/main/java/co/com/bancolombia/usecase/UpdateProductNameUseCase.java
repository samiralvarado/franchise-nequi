package co.com.bancolombia.usecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.BranchNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.FranchiseNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.InvalidProductException;
import co.com.bancolombia.model.franchise.exceptions.ProductNotFoundException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.values.ProductName;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UpdateProductNameUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> updateProductName(String fId, String bId, String pId, String newName) {
        return Mono.justOrEmpty(newName)
                .switchIfEmpty(Mono.error(new InvalidProductException("Product name is required")))
                .flatMap(name -> Mono.fromCallable(() -> ProductName.of(name)))
                .onErrorMap(IllegalArgumentException.class, ex -> new InvalidProductException(ex.getMessage()))
                .flatMap(validProductName -> franchiseRepository.findById(fId)
                        .switchIfEmpty(Mono.error(new FranchiseNotFoundException()))
                        .map(franchise -> {
                            List<Branch> branches = franchise.getBranches() != null
                                    ? franchise.getBranches()
                                    : new ArrayList<>();

                            Branch targetBranch = branches.stream()
                                    .filter(b -> b.getId().equals(bId))
                                    .findFirst()
                                    .orElseThrow(BranchNotFoundException::new);

                            List<Product> products = targetBranch.getProducts() != null
                                    ? targetBranch.getProducts()
                                    : new ArrayList<>();

                            Product targetProduct = products.stream()
                                    .filter(p -> p.getId().equals(pId))
                                    .findFirst()
                                    .orElseThrow(ProductNotFoundException::new);

                            Product updatedProduct = targetProduct.toBuilder()
                                    .name(validProductName)
                                    .build();

                            List<Product> updatedProducts = products.stream()
                                    .map(p -> p.getId().equals(pId) ? updatedProduct : p)
                                    .toList();

                            Branch updatedBranch = targetBranch.toBuilder()
                                    .products(updatedProducts)
                                    .build();

                            List<Branch> updatedBranches = branches.stream()
                                    .map(b -> b.getId().equals(bId) ? updatedBranch : b)
                                    .toList();

                            return franchise.toBuilder()
                                    .branches(updatedBranches)
                                    .build();
                        })
                )
                .flatMap(franchiseRepository::save);
    }
}
