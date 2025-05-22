package jsl.group.catalog_service.exceptions;

import jsl.group.catalog_service.domain.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BookControllerAdvice {
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseMessage<String> bookNotFoundHandler(BookNotFoundException ex) {
        return new ResponseMessage<>(
                null, HttpStatus.NOT_FOUND.value(), null, null, LocalDateTime.now(), ex.getMessage(),true
        );
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ResponseMessage<String> bookAlreadyExistsHandler(BookAlreadyExistsException ex) {
        return new ResponseMessage<>(
                null, HttpStatus.UNPROCESSABLE_ENTITY.value(), null, null, LocalDateTime.now(), ex.getMessage(), true
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseMessage<>(
                null, HttpStatus.BAD_REQUEST.value(), null, null, LocalDateTime.now(), errors, true
        );
    }
}
