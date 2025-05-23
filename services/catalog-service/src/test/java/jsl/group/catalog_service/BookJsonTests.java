package jsl.group.catalog_service;

import jsl.group.catalog_service.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest(properties = {"spring.cloud.config.enabled=false"})
public class BookJsonTests {
    @Autowired
    private JacksonTester<Book> bookJacksonTester;

    @Test
    void bookSerialize() throws IOException {
        Book book = Book.of("ISBN-10 0-596-52068-9", "Title", "Author", BigDecimal.valueOf(9.90), "Springer");
        JsonContent<Book> jsonContent = bookJacksonTester.write(book);
        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn").isEqualTo(book.isbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title").isEqualTo(book.title());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author").isEqualTo(book.author());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price").isEqualTo(book.price().doubleValue());
    }

    @Test
    void bookDeserialize() throws IOException {
        String content = """
                {
                    "isbn": "ISBN-10 0-596-52068-9",
                    "title": "Title",
                    "author": "Author",
                    "price": 9.9,
                    "publisher": "Springer"
                }
                """;
        assertThat(bookJacksonTester.parse(content)).usingRecursiveComparison()
                .isEqualTo(Book.of("ISBN-10 0-596-52068-9", "Title", "Author", BigDecimal.valueOf(9.9), "Springer"));
    }
}
