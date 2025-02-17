package com.fiap.tech.challenge.domain.validation.handler;

import com.fiap.tech.challenge.domain.exceptions.DomainException;
import com.fiap.tech.challenge.domain.validation.Error;
import com.fiap.tech.challenge.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ThrowsValidationHandlerTest {

    @Test
    void givenAnError_whenAppend_thenShouldThrowDomainException() {
        // Arrange
        ValidationHandler handler = new ThrowsValidationHandler();
        Error error = new Error("Test error");

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> handler.append(error));
        assertEquals("Test error", exception.getMessage());
    }

    @Test
    void givenAValidationHandler_whenAppend_thenShouldThrowDomainException() {
        // Arrange
        ValidationHandler handler = new ThrowsValidationHandler();
        ValidationHandler anotherHandler = new ValidationHandler() {
            @Override
            public ValidationHandler append(Error anError) {
                return this;
            }

            @Override
            public ValidationHandler append(ValidationHandler aHandler) {
                return this;
            }

            @Override
            public <T> T validate(Validation<T> aValidation) {
                return null;
            }

            @Override
            public List<Error> getErrors() {
                return List.of(new Error("Another handler error"));
            }
        };

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> handler.append(anotherHandler));
        assertEquals("Another handler error", exception.getErrors().get(0).message());
    }
    @Test
    void givenAValidationThatThrowsException_whenValidate_thenShouldThrowDomainException() {
        // Arrange
        ValidationHandler handler = new ThrowsValidationHandler();

        ValidationHandler.Validation<String> failingValidation = () -> {
            throw new IllegalArgumentException("Validation failed");
        };

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> handler.validate(failingValidation));
        assertEquals("Validation failed", exception.getMessage());
    }

    @Test
    void whenGetErrors_thenShouldReturnEmptyList() {
        // Arrange
        ValidationHandler handler = new ThrowsValidationHandler();

        // Act
        List<Error> errors = handler.getErrors();

        // Assert
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }


}