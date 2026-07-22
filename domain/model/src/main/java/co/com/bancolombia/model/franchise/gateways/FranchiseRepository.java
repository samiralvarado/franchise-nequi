package co.com.bancolombia.model.franchise.gateways;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.product.dto.ProductReport;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(String id);
    Flux<ProductReport> getMaxStockPerBranch(String franchiseId);
    Mono<Franchise> findByName(String name);

}
