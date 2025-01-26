package com.fiap.tech.challenge.application.production.retrieve.get;

import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.domain.production.ProductionStatus;

import java.time.Instant;
import java.util.List;

public record ProductionOutput(
        String id,
        ProductionStatus status,
        Instant receivedAt,
        Instant startedAt,
        Instant finishedAt,
        List<ItemOutput> items
) {

    public static ProductionOutput from(final Production aProduction) {
        return new ProductionOutput(
                aProduction.getId().getValue(),
                aProduction.getStatus(),
                aProduction.getReceivedAt(),
                aProduction.getStartedAt(),
                aProduction.getFinishedAt(),
                extractItems(aProduction)
        );
    }

    private static List<ItemOutput> extractItems(final Production aProduction) {
        return aProduction.getItems()
                .stream()
                .map(ItemOutput::from)
                .toList();
    }
}
