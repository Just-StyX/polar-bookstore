package jsl.group.order_service.web;

import jakarta.validation.Valid;
import jsl.group.order_service.domain.Order;
import jsl.group.order_service.domain.OrderService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Order> getAllOrders() { return orderService.getAllOrders(); }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Order> submitOrder(@RequestBody @Valid OrderRequest orderRequest) {
        return orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity());
    }
}
