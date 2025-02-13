package com.fiap.tech.challenge.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;

public final class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, query, cb) -> {
            Expression<String> enumAsString = cb.upper(root.get(prop).as(String.class));
            return cb.like(enumAsString, SqlUtils.like(term.toUpperCase()));
        };
    }


    public static <T> Specification<T> notEqual(final String prop, final Object value) {
        return (root, query, cb) -> cb.notEqual(root.get(prop), value);
    }

    public static <T> Specification<T> notEqual(final String prop, final Enum<?> value) {
        return (root, query, cb) -> cb.notEqual(root.get(prop), value);
    }
}
