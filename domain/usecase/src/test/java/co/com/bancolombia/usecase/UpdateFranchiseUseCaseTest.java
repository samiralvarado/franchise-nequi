package co.com.bancolombia.usecase;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.InvalidFranchiseException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.franchise.values.FranchiseName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateFranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private UpdateFranchiseUseCase useCase;

    @Test
    void shouldUpdateFranchiseName() {
        Franchise franchise = Franchise.builder().id("franchise-id").name(FranchiseName.of("Old name")).build();

        when(franchiseRepository.findById("franchise-id")).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.execute("franchise-id", "New name"))
                .expectNextMatches(saved -> saved.getName().getValue().equals("New name"))
                .verifyComplete();
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        Franchise franchise = Franchise.builder().id("franchise-id").name(FranchiseName.of("Old name")).build();

        when(franchiseRepository.findById("franchise-id")).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.execute("franchise-id", " "))
                .expectError(InvalidFranchiseException.class)
                .verify();
    }
}