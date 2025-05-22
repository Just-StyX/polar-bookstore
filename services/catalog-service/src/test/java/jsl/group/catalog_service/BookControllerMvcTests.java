package jsl.group.catalog_service;

import jsl.group.catalog_service.domain.BookService;
import jsl.group.catalog_service.exceptions.BookNotFoundException;
import jsl.group.catalog_service.web.BookController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerMvcTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BookService bookService;

    @Test
    void whenGetNotExistShouldReturn404() throws Exception {
        String isbn = "12345678";
        given(bookService.vewBookDetails(isbn)).willThrow(BookNotFoundException.class);
        mockMvc.perform(get("/books/" + isbn)).andExpect(status().isNotFound());
    }
}
