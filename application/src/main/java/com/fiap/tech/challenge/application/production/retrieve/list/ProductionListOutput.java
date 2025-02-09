package com.fiap.tech.challenge.application.production.retrieve.list;

import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.domain.production.ProductionStatus;

import java.time.Instant;

public record ProductionListOutput(
        String id,
        String orderId,
        ProductionStatus status,
        Instant receivedAt,
        Instant startedAt
) {

    public static ProductionListOutput from(final Production aProduction) {
        return new ProductionListOutput(
                aProduction.getId().getValue(),
                aProduction.getOrderId(),
                aProduction.getStatus(),
                aProduction.getReceivedAt(),
                aProduction.getStartedAt()
        );
    }
}
