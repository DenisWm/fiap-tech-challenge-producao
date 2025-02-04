package com.fiap.tech.challenge.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, query, cb) -> cb.like(cb.upper(root.get(prop)), SqlUtils.like(term.toUpperCase()));
    }

    public static <T> Specification<T> notEqual(final String prop, final Object value) {
        return (root, query, cb) -> cb.notEqual(root.get(prop), value);
    }

    public static <T> Specification<T> notEqual(final String prop, final Enum<?> value) {
        return (root, query, cb) -> cb.notEqual(root.get(prop), value);
    }
}
