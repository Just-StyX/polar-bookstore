package jsl.group.catalog_service.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.math.BigDecimal;
import java.time.Instant;

public record Book(
        @Id
        Long id,
        @Version
        int version,
        @NotBlank(message = "The book ISBN must be defined")
        @Pattern(
                regexp = "^(?:ISBN(?:-10)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
                message = "The ISBN format is invalid"
        )
        String isbn,
        @NotBlank(message = "What is the title of the book? Please provide")
        String title,
        @NotBlank(message = "Who is the author of this book? Please provide")
        String author,
        @NotNull(message = "What is the price of the book? Please provide")
        @Positive(message = "The price must be greater than zero(0)")
        BigDecimal price,
        String publisher,

        @CreatedDate
        Instant createdDate,
        @LastModifiedDate
        Instant lastModifiedDate
) {
        public static Book of(String isbn, String title, String author, BigDecimal price, String publisher) {
                return new Book(null, 0, isbn, title, author, price, publisher, null, null);
        }
}
