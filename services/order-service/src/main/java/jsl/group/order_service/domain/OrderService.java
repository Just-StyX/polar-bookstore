package jsl.group.order_service.domain;

import jsl.group.order_service.book.Book;
import jsl.group.order_service.book.BookClient;
import jsl.group.order_service.config.OrderConfigurationProperties;
import jsl.group.order_service.event.OrderAcceptedMessage;
import jsl.group.order_service.event.OrderDispatchedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final BookClient bookClient;
    private final StreamBridge streamBridge;
    private final OrderRepository orderRepository;
    private final OrderConfigurationProperties orderConfigurationProperties;

    public OrderService(BookClient bookClient, StreamBridge streamBridge, OrderRepository orderRepository, OrderConfigurationProperties orderConfigurationProperties) {
        this.bookClient = bookClient;
        this.streamBridge = streamBridge;
        this.orderRepository = orderRepository;
        this.orderConfigurationProperties = orderConfigurationProperties;
    }

    public Mono<ResponseMessage<List<Order>>> getAllOrders() {
        return orderRepository.findAll().collectList().map(orderList -> new ResponseMessage<>(
                orderConfigurationProperties.getVersion(), HttpStatus.OK.value(), HttpMethod.GET.name(), null, LocalDateTime.now(), orderList, false
        ));
    }
    @Transactional
    public Mono<ResponseMessage<Order>> submitOrder(String bookIsbn, int quantity) {
        return bookClient.getBookByIsbn(bookIsbn)
                .map(book -> buildAcceptedOrder(book, quantity))
                .defaultIfEmpty(buildRejectedOrder(bookIsbn, quantity))
                .flatMap(orderRepository::save)
                .map(order -> {
                    publishOrderAcceptedEvent(order);
                    return new ResponseMessage<>(
                            orderConfigurationProperties.getVersion(), HttpStatus.CREATED.value(), HttpMethod.POST.name(), null, LocalDateTime.now(), order, false
                    );
                });
    }

    public Flux<Order> consumerOrderDispatchedEvent(Flux<OrderDispatchedMessage> orderDispatchedMessageFlux) {
        return orderDispatchedMessageFlux.flatMap(message -> orderRepository.findById(message.orderId()))
                .map(this::buildDispatchedOrder)
                .flatMap(orderRepository::save);
    }

    public static Order buildRejectedOrder(String bookIsbn, int quantity) {
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }

    public static Order buildAcceptedOrder(Book book, int quantity) {
        return Order.of(book.isbn(), book.title() + " - " + book.author(), book.price(), quantity, OrderStatus.ACCEPTED);
    }

    private Order buildDispatchedOrder(Order order) {
        return new Order(
                order.id(), order.version(), order.bookIsbn(), order.bookName(), order.bookPrice(), order.quantity(), order.status(), order.createdDate(), order.lastModifiedDate()
        );
    }

    private void publishOrderAcceptedEvent(Order order) {
        if (!order.status().equals(OrderStatus.ACCEPTED)) return;
        OrderAcceptedMessage orderAcceptedMessage = new OrderAcceptedMessage(order.id());
        log.info("Sending order accepted event with id: {}", order.id());
        var result = streamBridge.send("acceptedOrder-out-0", orderAcceptedMessage);
        log.info("Result of sending data fro order with id {}: {}", order.id(), result);
    }
}
