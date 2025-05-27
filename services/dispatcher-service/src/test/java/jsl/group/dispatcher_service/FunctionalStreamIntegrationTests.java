package jsl.group.dispatcher_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jsl.group.dispatcher_service.domain.OrderAcceptMessage;
import jsl.group.dispatcher_service.domain.OrderDispatchMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false"})
@Import(TestChannelBinderConfiguration.class)
public class FunctionalStreamIntegrationTests {
    @Autowired
    private InputDestination inputDestination;
    @Autowired
    private OutputDestination outputDestination;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void acceptAndDispatch() throws IOException {
        long orderId = 121;
        Message<OrderAcceptMessage> orderAcceptMessageMessage = MessageBuilder.withPayload(new OrderAcceptMessage(orderId)).build();
        Message<OrderDispatchMessage> orderDispatchMessageMessage = MessageBuilder.withPayload(new OrderDispatchMessage(orderId)).build();

        this.inputDestination.send(orderAcceptMessageMessage);
        assertThat(objectMapper.readValue(outputDestination.receive().getPayload(), OrderDispatchMessage.class))
                .isEqualTo(orderDispatchMessageMessage.getPayload());
    }
}
