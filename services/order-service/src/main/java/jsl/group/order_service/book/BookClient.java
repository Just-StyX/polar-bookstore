package jsl.group.order_service.book;

import jsl.group.order_service.domain.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.lang.reflect.Type;
import java.time.Duration;

@Component
public class BookClient {
    private final static String BOOK_ROOT_API = "/books/";
    private static final Logger log = LoggerFactory.getLogger(BookClient.class);
    private final WebClient webClient;

    public BookClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Book> getBookByIsbn(String bookIsbn) {
        ParameterizedTypeReference<ResponseMessage<Book>> parameterizedTypeReference = new ParameterizedTypeReference<>() {
            @Override
            @NonNull
            public Type getType() {
                return super.getType();
            }
        };
        return webClient.get().uri(BOOK_ROOT_API + bookIsbn).retrieve().bodyToMono(parameterizedTypeReference)
                .map(ResponseMessage::body)
                .timeout(Duration.ofSeconds(3), Mono.empty())
                .onErrorResume(WebClientResponseException.NotFound.class, exception -> Mono.empty())
                .retryWhen(
                        Retry.backoff(3, Duration.ofMillis(100))
                )
                .onErrorResume(Exception.class, exception -> Mono.empty());
    }
}
