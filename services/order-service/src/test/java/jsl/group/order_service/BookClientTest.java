package jsl.group.order_service;

import jsl.group.order_service.book.Book;
import jsl.group.order_service.book.BookClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

public class BookClientTest {
    private MockWebServer mockWebServer;
    private BookClient bookClient;

    @BeforeEach
    void setBookClient() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        var webClient = WebClient.builder().baseUrl(mockWebServer.url("/").uri().toString()).build();
        this.bookClient = new BookClient(webClient);
    }

    @AfterEach
    void clean() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    void returnsBookWhenBookExists() {
        String bookIsbn = "ISBN-10 0-596-52068-9";
        String content = """
                {
                    "body": {
                                "isbn": %s,
                                "title": "Title",
                                "author": "Author",
                                "price": 9.9,
                                "publisher": "Springer"
                            },
                    "version": 1,
                    "httpStatus": 201,
                    "httpMethod": "POST",
                    "url": "http://",
                    "localDateTime": "",
                    "exception": false
                }
                """.formatted(bookIsbn);
        var mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(content);
        mockWebServer.enqueue(mockResponse);
        Mono<Book> messageMono = bookClient.getBookByIsbn(bookIsbn);
        StepVerifier.create(messageMono).expectNextMatches(book -> book.isbn().equals(bookIsbn));
    }
}
