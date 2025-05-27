package jsl.group.dispatcher_service.config;

import jsl.group.dispatcher_service.domain.OrderAcceptMessage;
import jsl.group.dispatcher_service.domain.OrderDispatchMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
public class DispatchingFunctionsConfiguration {
    private static final Logger log = LoggerFactory.getLogger(DispatchingFunctionsConfiguration.class);

    @Bean
    public Function<OrderAcceptMessage, Long> pack() {
        return orderAcceptMessage -> {
            log.info("The order with ID {} is packed.", orderAcceptMessage.orderId());
            return orderAcceptMessage.orderId();
        };
    }

    @Bean
    public Function<Flux<Long>, Flux<OrderDispatchMessage>> label() {
        return longFlux -> longFlux.map(orderId -> {
            log.info("The order with ID {} is labeled.", orderId);
            return new OrderDispatchMessage(orderId);
        });
    }
}
