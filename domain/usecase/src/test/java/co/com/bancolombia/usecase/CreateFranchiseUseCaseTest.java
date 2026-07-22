package co.com.bancolombia.usecase;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.exceptions.FranchiseAlreadyExistsException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateFranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private CreateFranchiseUseCase useCase;

    @Test
    void shouldCreateFranchise() {
        Franchise input = Franchise.builder().name(FranchiseName.of("North Franchise")).build();
        Franchise saved = Franchise.builder().id("franchise-id").name(FranchiseName.of("North Franchise")).build();

        when(franchiseRepository.findByName("North Franchise")).thenReturn(Mono.empty());
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(useCase.execute(input))
                .expectNextMatches(franchise -> franchise.getId().equals("franchise-id"))
                .verifyComplete();

        verify(franchiseRepository).save(any(Franchise.class));
    }

    @Test
    void shouldRejectDuplicatedFranchiseName() {
        Franchise input = Franchise.builder().name(FranchiseName.of("North Franchise")).build();
        Franchise existing = Franchise.builder().id("existing-id").name(FranchiseName.of("North Franchise")).build();

        when(franchiseRepository.findByName("North Franchise")).thenReturn(Mono.just(existing));

        StepVerifier.create(useCase.execute(input))
                .expectError(FranchiseAlreadyExistsException.class)
                .verify();
    }
}