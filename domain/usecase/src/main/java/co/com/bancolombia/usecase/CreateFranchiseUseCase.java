package co.com.bancolombia.usecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.FranchiseAlreadyExistsException;
import co.com.bancolombia.model.franchise.exceptions.InvalidFranchiseException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CreateFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(Franchise franchise) {
        return Mono.just(franchise)
                .flatMap(f -> Mono.justOrEmpty(f.getName())
                        .switchIfEmpty(Mono.error(new InvalidFranchiseException("Franchise name is required")))
                        .thenReturn(f))
                .flatMap(f -> franchiseRepository.findByName(f.getName().getValue())
                        .flatMap(existing -> Mono.<Franchise>error(new FranchiseAlreadyExistsException()))
                        .defaultIfEmpty(f))
                .map(f -> {
                    List<Branch> branches = f.getBranches() != null ? f.getBranches() : new ArrayList<>();
                    return f.toBuilder()
                            .branches(branches)
                            .build();
                })
                .flatMap(franchiseRepository::save);
    }
}
