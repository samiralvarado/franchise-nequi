package co.com.bancolombia.franchise_api.infrastructure.entrypoints.router;

import co.com.bancolombia.franchise_api.infrastructure.entrypoints.handler.FranchiseHandler;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class FranchiseRouterTest {

    @Test
    void shouldCreateRouterFunction() {
        FranchiseRouter router = new FranchiseRouter();
        FranchiseHandler handler = mock(FranchiseHandler.class);

        RouterFunction<ServerResponse> route = router.route(handler);

        assertNotNull(route);
    }
}
