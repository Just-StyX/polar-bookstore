package jsl.group.catalog_service.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record Book(
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
        BigDecimal price
) {
}
