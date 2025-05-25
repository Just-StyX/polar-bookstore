package jsl.group.order_service.domain;

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
    public static <T> ResponseMessage<T> of(ResponseMessage<T> responseMessage, String location) {
        return new ResponseMessage<>(
                responseMessage.version(),
                responseMessage.httpStatus(),
                responseMessage.httpMethod(),
                location,
                responseMessage.localDateTime(),
                responseMessage.body(),
                responseMessage.exception()
        );
    }
}
