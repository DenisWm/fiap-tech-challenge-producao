package com.fiap.tech.challenge.domain.production;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum ProductionStatus {
    RECEIVED, IN_PREPARATION, READY;


    public static Optional<ProductionStatus> of(final String status) {
        return Arrays
                .stream(values())
                .filter(productionStatus -> productionStatus.name().equals(status))
                .findFirst();
    }

    public static List<ProductionStatus> matching(final String partial) {
        return Arrays.stream(values())
                .filter(status -> status.name().contains(partial.toUpperCase()))
                .toList();
    }
}
