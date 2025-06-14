package jsl.group.catalog_service.domain;

import jsl.group.catalog_service.exceptions.BookAlreadyExistsException;
import jsl.group.catalog_service.exceptions.BookNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Iterable<Book> viewBookList() {
        return bookRepository.findAll();
    }

    public Book vewBookDetails(String isbn) {
        return bookRepository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public Book addBookToCatalog(Book book) {
        if (bookRepository.existsByIsbn(book.isbn())) throw new BookAlreadyExistsException(book.isbn());
        return bookRepository.save(book);
    }

    public void removeBookFromCatalog(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }

    public Book editBookDetails(String isbn, Book book) {
        return bookRepository.findByIsbn(isbn)
                .map(existingBook -> {
                    var bookToUpdate = new Book(existingBook.id(), existingBook.version(), existingBook.isbn(), book.title(),
                            book.author(), book.price(), existingBook.publisher(), existingBook.createdDate(), existingBook.lastModifiedDate(),
                            existingBook.createdBy(), existingBook.lastModifiedBy()
                    );
                    return bookRepository.save(bookToUpdate);
                }).orElseGet(() -> addBookToCatalog(book));
    }
}
