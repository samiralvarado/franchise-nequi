package co.com.bancolombia.usecase;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.FranchiseNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.InvalidFranchiseException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.franchise.values.FranchiseName;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(String id, String newName) {
        return Mono.justOrEmpty(newName)
                .switchIfEmpty(Mono.error(new InvalidFranchiseException("Franchise name is required")))
                .flatMap(name -> Mono.fromCallable(() -> FranchiseName.of(name)))
                .onErrorMap(IllegalArgumentException.class, ex -> new InvalidFranchiseException(ex.getMessage()))
                .flatMap(validFranchiseName -> franchiseRepository.findById(id)
                        .switchIfEmpty(Mono.error(new FranchiseNotFoundException()))
                        .map(franchise -> franchise.toBuilder()
                                .name(validFranchiseName)
                                .build())
                )
                .flatMap(franchiseRepository::save);
    }
}
