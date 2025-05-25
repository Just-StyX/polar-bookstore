package jsl.group.order_service.domain;

import jsl.group.order_service.config.OrderConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderConfigurationProperties orderConfigurationProperties;

    public OrderService(OrderRepository orderRepository, OrderConfigurationProperties orderConfigurationProperties) {
        this.orderRepository = orderRepository;
        this.orderConfigurationProperties = orderConfigurationProperties;
    }

    public Mono<ResponseMessage<List<Order>>> getAllOrders() {
        return orderRepository.findAll().collectList().map(orderList -> new ResponseMessage<>(
                orderConfigurationProperties.getVersion(), HttpStatus.OK.value(), HttpMethod.GET.name(), null, LocalDateTime.now(), orderList, false
        ));
    }
    public Mono<ResponseMessage<Order>> submitOrder(String bookIsbn, int quantity) {
        return Mono.just(buildRejectedOrder(bookIsbn, quantity)).flatMap(orderRepository::save)
                .map(order -> new ResponseMessage<>(
                        orderConfigurationProperties.getVersion(), HttpStatus.CREATED.value(), HttpMethod.POST.name(), null, LocalDateTime.now(), order, false
                ));
    }

    private static Order buildRejectedOrder(String bookIsbn, int quantity) {
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }
}
