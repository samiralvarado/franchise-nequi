package co.com.bancolombia.usecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.values.BranchName;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.BranchNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.FranchiseNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.InvalidBranchException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UpdateBranchNameUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> updateBranchName(String fId, String bId, String newName) {
        return Mono.justOrEmpty(newName)
                .switchIfEmpty(Mono.error(new InvalidBranchException("Branch name is required")))
                .flatMap(name -> Mono.fromCallable(() -> BranchName.of(name)))
                .onErrorMap(IllegalArgumentException.class, ex -> new InvalidBranchException(ex.getMessage()))
                .flatMap(validBranchName -> franchiseRepository.findById(fId)
                        .switchIfEmpty(Mono.error(new FranchiseNotFoundException()))
                        .flatMap(franchise -> {
                            List<Branch> branches = franchise.getBranches() != null
                                    ? franchise.getBranches()
                                    : new ArrayList<>();

                            Branch targetBranch = branches.stream()
                                    .filter(b -> b.getId().equals(bId))
                                    .findFirst()
                                    .orElseThrow(BranchNotFoundException::new);

                            Branch updatedBranch = targetBranch.toBuilder()
                                    .name(validBranchName)
                                    .build();

                            List<Branch> updatedBranches = branches.stream()
                                    .map(b -> b.getId().equals(bId) ? updatedBranch : b)
                                    .toList();

                            Franchise updatedFranchise = franchise.toBuilder()
                                    .branches(updatedBranches)
                                    .build();

                            return franchiseRepository.save(updatedFranchise);
                        })
                );
    }
}
