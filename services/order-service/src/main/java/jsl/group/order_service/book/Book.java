package jsl.group.order_service.book;

import java.math.BigDecimal;
import java.time.Instant;

public record Book(
        Long id,
        int version,
        String isbn,
        String title,
        String author,
        BigDecimal price,
        String publisher,
        Instant createdDate,
        Instant lastModifiedDate
) {
}
