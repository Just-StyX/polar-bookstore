package jsl.group.catalog_service;

import jsl.group.catalog_service.config.DataConfiguration;
import jsl.group.catalog_service.domain.Book;
import jsl.group.catalog_service.domain.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("integration")
@Import(DataConfiguration.class)
@DataJdbcTest(properties = {"spring.cloud.config.enabled=false"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryJdbcTests {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findWhenIsbnExists() {
        String isbn = "ISBN-10 0-596-52068-9";
        Book book = Book.of(isbn, "Title", "Author", BigDecimal.valueOf(9.90), "Springer");
        jdbcAggregateTemplate.insert(book);
        Optional<Book> actualBook = bookRepository.findByIsbn(isbn);

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().isbn()).isEqualTo(book.isbn());
    }

    @Test
    @WithMockUser("jsl")
    void authenticatedAndAuditData() {
        Book book = Book.of("ISBN-10 0-596-52068-9", "Title", "Author", BigDecimal.valueOf(9.90), "Springer");
        Book createdBook = bookRepository.save(book);
        assertThat(createdBook.createdBy()).isEqualTo("jsl");
        assertThat(createdBook.lastModifiedBy()).isEqualTo("jsl");
    }
}
