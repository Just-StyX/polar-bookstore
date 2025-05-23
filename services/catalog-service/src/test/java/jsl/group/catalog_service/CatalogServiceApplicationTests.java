package jsl.group.catalog_service;

import jsl.group.catalog_service.domain.Book;
import jsl.group.catalog_service.domain.ResponseMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.cloud.config.enabled=false"})
class CatalogServiceApplicationTests extends PostgresBaseTest {
	@Autowired
	private WebTestClient webTestClient;
	@Test
	void contextLoads() {
	}

	@Test
	void postRequestToCreateBook() {
		Book book = Book.of("ISBN-10 0-596-52068-9", "Title", "Author", BigDecimal.valueOf(9.90));
		webTestClient.post()
				.uri("/books")
				.bodyValue(book)
				.exchange();
//				.expectStatus().isCreated()
//				.expectBody(ResponseMessage.class).value(actual -> {
//					assertThat(actual.body()).isNotNull();
//					assertThat(actual.exception()).isFalse();
//					assertThat(actual.url()).isNotNull();
//					assertThat(actual.version()).isEqualTo("api-v1");
//				});
	}

}
