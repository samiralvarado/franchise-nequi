package co.com.bancolombia.usecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.values.BranchName;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.FranchiseNotFoundException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.franchise.values.FranchiseName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddBranchUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private AddBranchUseCase useCase;

    @Test
    void shouldAddBranchToExistingFranchise() {
        Franchise franchise = Franchise.builder()
                .id("franchise-id")
                .name(FranchiseName.of("North Franchise"))
                .branches(new ArrayList<>())
                .build();
        Branch branch = Branch.builder().name(BranchName.of("Main Branch")).build();

        when(franchiseRepository.findById("franchise-id")).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.execute("franchise-id", branch))
                .expectNextMatches(saved -> saved.getBranches().size() == 1
                        && saved.getBranches().getFirst().getId() != null)
                .verifyComplete();
    }

    @Test
    void shouldFailWhenFranchiseDoesNotExist() {
        when(franchiseRepository.findById("missing-id")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("missing-id", Branch.builder().name(BranchName.of("Main Branch")).build()))
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }
}