package co.com.bancolombia.usecase;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.BranchNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.FranchiseNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.ProductNotFoundException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(String franchiseId, String branchId, String productId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException()))
                .flatMap(franchise -> Flux.fromIterable(franchise.getBranches())
                        .filter(branch -> branch.getId().equals(branchId))
                        .next()
                        .switchIfEmpty(Mono.error(new BranchNotFoundException()))
                        .flatMap(branch -> Flux.fromIterable(branch.getProducts())
                                .filter(product -> product.getId().equals(productId))
                                .next()
                                .switchIfEmpty(Mono.error(new ProductNotFoundException()))
                                .map(product -> {
                                    branch.getProducts().remove(product);
                                    return franchise;
                                })
                        )
                )
                .flatMap(franchiseRepository::save);
    }
}
