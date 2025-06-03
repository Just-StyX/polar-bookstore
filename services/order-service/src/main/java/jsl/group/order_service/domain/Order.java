package jsl.group.order_service.domain;

import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
@Table("orders")
public record Order(
        @Id
        Long id,
        @Version
        int version,

        String bookIsbn,
        String bookName,
        BigDecimal bookPrice,
        Integer quantity,
        OrderStatus status,

        @CreatedDate
        Instant createdDate,
        @LastModifiedDate
        Instant lastModifiedDate,
        @CreatedBy
        String createdBy,
        @LastModifiedBy
        String lastModifiedBy
) {
    public static Order of(String bookIsbn, String bookName, BigDecimal bookPrice, Integer quantity, OrderStatus status) {
        return new Order(null, 0, bookIsbn, bookName, bookPrice, quantity, status, null, null, null, null);
    }
}
