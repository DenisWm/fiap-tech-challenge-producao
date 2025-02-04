package com.fiap.tech.challenge.domain.production;

import com.fiap.tech.challenge.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class ProductionID extends Identifier {

    private final String value;

    private ProductionID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static ProductionID unique() {
        return ProductionID.from(UUID.randomUUID().toString().toLowerCase());
    }

    public static ProductionID from(final String anId) {
        return new ProductionID(anId);
    }

    public static ProductionID from(final UUID anId) {
        return new ProductionID(anId.toString());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (ProductionID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
