package co.com.bancolombia.usecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.values.BranchName;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.BranchNotFoundException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateBranchNameUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private UpdateBranchNameUseCase useCase;

    @Test
    void shouldUpdateBranchName() {
        Branch branch = Branch.builder().id("branch-id").name(BranchName.of("Old branch")).build();
        Franchise franchise = Franchise.builder().id("franchise-id").branches(List.of(branch)).build();

        when(franchiseRepository.findById("franchise-id")).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.updateBranchName("franchise-id", "branch-id", "New branch"))
                .expectNextMatches(saved -> saved.getBranches().getFirst().getName().getValue().equals("New branch"))
                .verifyComplete();
    }

    @Test
    void shouldFailWhenBranchDoesNotExist() {
        Franchise franchise = Franchise.builder().id("franchise-id").branches(List.of()).build();

        when(franchiseRepository.findById("franchise-id")).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.updateBranchName("franchise-id", "missing-id", "New branch"))
                .expectError(BranchNotFoundException.class)
                .verify();
    }
}