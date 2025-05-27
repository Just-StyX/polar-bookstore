package jsl.group.order_service.config;

import jsl.group.order_service.domain.OrderService;
import jsl.group.order_service.event.OrderDispatchedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
public class OrderFunctions {
    private static final Logger log = LoggerFactory.getLogger(OrderFunctions.class);

    @Bean
    public Consumer<Flux<OrderDispatchedMessage>> dispatchOrder(OrderService orderService) {
        return orderDispatchedMessageFlux -> orderService.consumerOrderDispatchedEvent(orderDispatchedMessageFlux)
                .doOnNext(order -> log.info("The order with id {} is dispatched", order.id())).subscribe();
    }
}
