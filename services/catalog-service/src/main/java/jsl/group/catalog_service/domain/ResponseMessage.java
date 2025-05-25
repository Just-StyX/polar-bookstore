package jsl.group.catalog_service.domain;

import java.time.LocalDateTime;

public record ResponseMessage<T> (
        String version,
        int httpStatus,
        String httpMethod,
        String url,
        LocalDateTime localDateTime,
        T body,
        boolean exception
) {
}
