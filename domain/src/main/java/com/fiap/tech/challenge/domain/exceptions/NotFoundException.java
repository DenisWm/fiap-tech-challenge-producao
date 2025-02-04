package com.fiap.tech.challenge.domain.exceptions;

import com.fiap.tech.challenge.domain.AggregateRoot;
import com.fiap.tech.challenge.domain.Identifier;
import com.fiap.tech.challenge.domain.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException{

    protected NotFoundException(String aMessage, List<com.fiap.tech.challenge.domain.validation.Error> anErrors) {
        super(aMessage, anErrors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> anAggregate,
            final Identifier id
    ) {
        final var anErrorMessage = "%s with ID %s was not found".formatted(anAggregate.getSimpleName(), id.getValue());
        return new NotFoundException(anErrorMessage, Collections.emptyList());
    }

    public static NotFoundException with(final Error error) {
        return new NotFoundException(error.message(), Collections.emptyList());
    }


}
