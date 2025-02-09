package com.fiap.tech.challenge.domain.validation.handler;

import com.fiap.tech.challenge.domain.exceptions.DomainException;
import com.fiap.tech.challenge.domain.validation.Error;
import com.fiap.tech.challenge.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(com.fiap.tech.challenge.domain.validation.Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(ValidationHandler aHandler) {
        throw DomainException.with(aHandler.getErrors());

    }

    @Override
    public <T> T validate(final Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (final Exception ex) {
            throw DomainException.with(new com.fiap.tech.challenge.domain.validation.Error(ex.getMessage()));
        }
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}

