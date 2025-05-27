package jsl.group.dispatcher_service;

import jsl.group.dispatcher_service.domain.OrderAcceptMessage;
import jsl.group.dispatcher_service.domain.OrderDispatchMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.function.Function;

@FunctionalSpringBootTest(properties = {"spring.cloud.config.enabled=false"})
public class DispatchingFunctionIntegrationTests {
    @Autowired
    private FunctionCatalog functionCatalog;

//    @Test
    public void orderPackedAndLabeled() {
        Function<OrderAcceptMessage, Flux<OrderDispatchMessage>> packComposeLabel = functionCatalog.lookup(Function.class, "pack|label");
        long orderId =121;
        StepVerifier.create(packComposeLabel.apply(new OrderAcceptMessage(orderId)))
                .expectNextMatches(orderDispatchMessage -> orderDispatchMessage.equals(new OrderDispatchMessage(orderId)))
                .verifyComplete();
    }
}
