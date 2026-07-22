package co.com.bancolombia.usecase;


import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.BranchAlreadyExistsException;
import co.com.bancolombia.model.franchise.exceptions.FranchiseNotFoundException;
import co.com.bancolombia.model.franchise.exceptions.InvalidBranchException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AddBranchUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(String franchiseId, Branch branch) {
        return Mono.just(branch)
                .flatMap(b -> Mono.fromCallable(() -> {
                    if (b.getName() == null) {
                        throw new InvalidBranchException("Branch name is required");
                    }
                    return b;
                }))
                .flatMap(validBranch -> franchiseRepository.findById(franchiseId)
                        .switchIfEmpty(Mono.error(new FranchiseNotFoundException()))
                        .flatMap(franchise -> {
                            List<Branch> currentBranches = franchise.getBranches() != null
                                    ? franchise.getBranches()
                                    : new ArrayList<>();

                            boolean exists = currentBranches.stream()
                                    .anyMatch(b -> b.getName().getValue().equalsIgnoreCase(validBranch.getName().getValue()));

                            return Mono.just(exists)
                                    .filter(exist -> !exist)
                                    .switchIfEmpty(Mono.error(new BranchAlreadyExistsException()))
                                    .map(ignored -> {
                                        Branch branchToSave = validBranch.toBuilder()
                                                .id(validBranch.getId() == null || validBranch.getId().isBlank()
                                                        ? UUID.randomUUID().toString()
                                                        : validBranch.getId())
                                                .build();

                                        List<Branch> updatedBranches = new ArrayList<>(currentBranches);
                                        updatedBranches.add(branchToSave);

                                        return franchise.toBuilder()
                                                .branches(updatedBranches)
                                                .build();
                                    });
                        })
                )
                .flatMap(franchiseRepository::save);
    }
}
