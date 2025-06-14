package jsl.group.order_service;

import jsl.group.order_service.config.OrderServiceSecurityConfiguration;
import jsl.group.order_service.domain.OrderService;
import jsl.group.order_service.domain.ResponseMessage;
import jsl.group.order_service.web.OrderController;
import jsl.group.order_service.web.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

@WebFluxTest(value = OrderController.class, properties = {"spring.cloud.config.enabled=false"})
@Import(OrderServiceSecurityConfiguration.class)
public class OrderControllerWebfluxTests {
    @Autowired
    private WebTestClient webTestClient;
    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    ReactiveJwtDecoder reactiveJwtDecoder;

    @Test
    void bookNotFound() {
        OrderRequest orderRequest = new OrderRequest("1234567890", 3);
        var expectedOrder = OrderService.buildRejectedOrder(orderRequest.isbn(), orderRequest.quantity());
        var message = new ResponseMessage<>(null, 0, null, null, null, expectedOrder, false);
        given(orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity()))
                .willReturn(Mono.just(message));
        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_customer")))
                .post().uri("/orders/").bodyValue(message).exchange().expectStatus().isNotFound();
    }
}
