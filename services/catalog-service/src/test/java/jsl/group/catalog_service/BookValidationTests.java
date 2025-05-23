package jsl.group.catalog_service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jsl.group.catalog_service.domain.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BookValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validationSucceedsWhenAllFieldAreValidatedRightly() {
        Book book = Book.of("ISBN-10 0-596-52068-9", "Title", "Author", BigDecimal.valueOf(9.90), "Springer");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void validationFailsWhenOneFieldValidationFails() {
        Book book = Book.of("ISBN-10 0-596-5", "Title", "Author", BigDecimal.valueOf(9.90), "Springer");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The ISBN format is invalid");
    }

    @Test
    void validationFailsWhenFieldValidationFails() {
        Book book = Book.of("", "", "Author", BigDecimal.valueOf(9.90), "Springer");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(3);
        assertThat(violations.stream().map(ConstraintViolation::getMessage)).contains("What is the title of the book? Please provide", "The book ISBN must be defined");
    }
}
