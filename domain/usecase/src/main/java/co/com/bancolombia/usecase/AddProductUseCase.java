package co.com.bancolombia.usecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.BranchNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.FranchiseNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.InvalidProductException;
import co.com.bancolombia.model.franchise.exceptions.ProductAlreadyExistsException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.Product;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AddProductUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(String franchiseId, String branchId, Product product) {
        return Mono.just(product)
                .flatMap(p -> Mono.justOrEmpty(p.getName())
                        .switchIfEmpty(Mono.error(new InvalidProductException("Product name is required")))
                        .thenReturn(p))
                .flatMap(p -> Mono.justOrEmpty(p.getStock())
                        .switchIfEmpty(Mono.error(new InvalidProductException("Product stock must be zero or greater")))
                        .thenReturn(p))
                .flatMap(validProduct -> franchiseRepository.findById(franchiseId)
                        .switchIfEmpty(Mono.error(new FranchiseNotFoundException()))
                        .flatMap(franchise -> {
                            List<Branch> branches = franchise.getBranches() != null ? franchise.getBranches() : new ArrayList<>();

                            Branch targetBranch = branches.stream()
                                    .filter(b -> b.getId().equals(branchId))
                                    .findFirst()
                                    .orElseThrow(BranchNotFoundException::new);

                            List<Product> currentProducts = targetBranch.getProducts() != null ? targetBranch.getProducts() : new ArrayList<>();

                            boolean productExists = currentProducts.stream()
                                    .anyMatch(p -> p.getName().getValue().equalsIgnoreCase(validProduct.getName().getValue()));

                            return Mono.just(productExists)
                                    .filter(exists -> !exists)
                                    .switchIfEmpty(Mono.error(new ProductAlreadyExistsException()))
                                    .map(ignored -> {
                                        Product productToSave = validProduct.toBuilder()
                                                .id(validProduct.getId() == null || validProduct.getId().isBlank()
                                                        ? UUID.randomUUID().toString()
                                                        : validProduct.getId())
                                                .build();

                                        List<Product> updatedProducts = new ArrayList<>(currentProducts);
                                        updatedProducts.add(productToSave);

                                        Branch updatedBranch = targetBranch.toBuilder()
                                                .products(updatedProducts)
                                                .build();

                                        List<Branch> updatedBranches = branches.stream()
                                                .map(b -> b.getId().equals(branchId) ? updatedBranch : b)
                                                .toList();

                                        return franchise.toBuilder()
                                                .branches(updatedBranches)
                                                .build();
                                    });
                        })
                )
                .flatMap(franchiseRepository::save);
    }
}
