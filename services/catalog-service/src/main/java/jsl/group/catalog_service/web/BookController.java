package jsl.group.catalog_service.web;

import jakarta.validation.Valid;
import jsl.group.catalog_service.domain.Book;
import jsl.group.catalog_service.domain.BookService;
import jsl.group.catalog_service.domain.ResponseMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("books")
public class BookController {
    private final String appVersion;
    private final BookService bookService;

    public BookController(BookService bookService, @Value("${app.version}") String appVersion) {
        this.appVersion = appVersion;
        this.bookService = bookService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage<Iterable<Book>> get(UriComponentsBuilder uriComponentsBuilder) {
        String location = uriComponentsBuilder.path("/books").buildAndExpand().toUri().toString();
        return new ResponseMessage<>(
                appVersion, HttpStatus.OK.value(), HttpMethod.GET.name(), location, LocalDateTime.now(), bookService.viewBookList(), false
        );
    }

    @GetMapping(value = "{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage<Book> getByIsbn(@PathVariable(value = "isbn") String isbn, UriComponentsBuilder uriComponentsBuilder) {
        String location = uriComponentsBuilder.path("/books/{isbn}").buildAndExpand(isbn).toUri().toString();
        return new ResponseMessage<>(
                appVersion, HttpStatus.OK.value(), HttpMethod.GET.name(), location, LocalDateTime.now(), bookService.vewBookDetails(isbn), false
        );
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessage<Book> post(@Valid @RequestBody Book book, UriComponentsBuilder uriComponentsBuilder) {
        String location = uriComponentsBuilder.path("/books").buildAndExpand().toUri().toString();
        return new ResponseMessage<>(
                appVersion, HttpStatus.CREATED.value(), HttpMethod.POST.name(), location, LocalDateTime.now(), bookService.addBookToCatalog(book), false
        );
    }

    @DeleteMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseMessage<Void> delete(@PathVariable String isbn, UriComponentsBuilder uriComponentsBuilder) {
        String location = uriComponentsBuilder.path("/books/{isbn}").buildAndExpand(isbn).toUri().toString();
        bookService.removeBookFromCatalog(isbn);
        return new ResponseMessage<>(
                appVersion, HttpStatus.NO_CONTENT.value(), HttpMethod.DELETE.name(), location, LocalDateTime.now(), null, false
        );
    }

    @PutMapping(value = "{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage<Book> put(@PathVariable(value = "isbn") String isbn, @Valid @RequestBody Book book, UriComponentsBuilder uriComponentsBuilder) {
        String location = uriComponentsBuilder.path("/books/{isbn}").buildAndExpand(isbn).toUri().toString();
        return new ResponseMessage<>(
                appVersion, HttpStatus.ACCEPTED.value(), HttpMethod.PUT.name(), location, LocalDateTime.now(), bookService.editBookDetails(isbn, book), false
        );
    }
}
