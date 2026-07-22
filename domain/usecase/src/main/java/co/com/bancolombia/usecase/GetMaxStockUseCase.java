package co.com.bancolombia.usecase;


import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.dto.ProductReport;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetMaxStockUseCase {
    private final FranchiseRepository repository;

    public Flux<ProductReport> execute(String franchiseId) {
        return repository.getMaxStockPerBranch(franchiseId);
    }
}