package jsl.group.order_service.web;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jsl.group.order_service.domain.Order;
import jsl.group.order_service.domain.OrderService;
import jsl.group.order_service.domain.ResponseMessage;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("orders")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Order Service API", description = "The order API generated with OpenApi")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseMessage<List<Order>>> getAllOrders(UriComponentsBuilder uriComponentsBuilder) {
        String location = uriComponentsBuilder.path("/orders").buildAndExpand().toUri().toString();
        return orderService.getAllOrders()
                .map(orderResponseMessage -> ResponseMessage.of(orderResponseMessage, location));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseMessage<Order>> submitOrder(@RequestBody @Valid OrderRequest orderRequest, UriComponentsBuilder uriComponentsBuilder) {
        String location = uriComponentsBuilder.path("/orders").buildAndExpand().toUri().toString();
        return orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity())
                .map(orderResponseMessage -> ResponseMessage.of(orderResponseMessage, location));
    }
}
