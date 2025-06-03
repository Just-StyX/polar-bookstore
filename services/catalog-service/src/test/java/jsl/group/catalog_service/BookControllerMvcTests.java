package jsl.group.catalog_service;

import jsl.group.catalog_service.config.CatalogSecurityConfiguration;
import jsl.group.catalog_service.config.PolarConfigurationProperties;
import jsl.group.catalog_service.domain.BookService;
import jsl.group.catalog_service.exceptions.BookNotFoundException;
import jsl.group.catalog_service.web.BookController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = BookController.class, properties = {"spring.cloud.config.enabled=false"})
@EnableConfigurationProperties(value = PolarConfigurationProperties.class)
@Import({CatalogSecurityConfiguration.class, CatalogJwtDecoderTest.class})
public class BookControllerMvcTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JwtDecoder jwtDecoder;
    @MockitoBean
    private BookService bookService;

    @Test
    void whenGetNotExistAndUnauthenticatedShouldReturn404() throws Exception {
        String isbn = "12345678";
        given(bookService.vewBookDetails(isbn)).willThrow(BookNotFoundException.class);
        mockMvc.perform(get("/books/" + isbn)).andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteBookWithCustomerRoleReturn403() throws Exception {
        var isbn = "ISBN-10 0-596-52068-9";
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + isbn).with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority(("ROLE_customer")))))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void UnauthenticatedCannotDelete() throws Exception {
        var isbn = "ISBN-10 0-596-52068-9";
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + isbn))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    // TODO: add keycloak container test
}
