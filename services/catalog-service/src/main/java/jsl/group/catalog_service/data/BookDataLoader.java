package jsl.group.catalog_service.data;

import jsl.group.catalog_service.domain.Book;
import jsl.group.catalog_service.domain.BookRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Profile("test-data")
public class BookDataLoader {
    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        Book bookOne = Book.of("ISBN-10 0-596-52068-9", "Title-one", "Author one", BigDecimal.valueOf(9.90), "Springer");
        Book bookTwo = Book.of("ISBN-10 0-596-52068-8", "Title-two", "Author two", BigDecimal.valueOf(9.90), "Springer");
        bookRepository.deleteAll();
        bookRepository.saveAll(List.of(bookOne, bookTwo));
    }
}
