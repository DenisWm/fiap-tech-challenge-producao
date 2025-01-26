package com.fiap.tech.challenge.domain.production;

import com.fiap.tech.challenge.domain.events.DomainEvent;

import java.time.Instant;

public class ProductionStatusChanged implements DomainEvent {

    String orderId;
    String status;
    Instant startedAt;
    Instant finishedAt;


    private ProductionStatusChanged(
            final String orderId,
            final String status,
            final Instant startedAt,
            final Instant finishedAt
    ) {
        this.orderId = orderId;
        this.status = status;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    public static ProductionStatusChanged with(
            final String orderId,
            final String status,
            final Instant startedAt,
            final Instant finishedAt
    ) {
        return new ProductionStatusChanged(
                orderId,
                status.toLowerCase(),
                startedAt,
                finishedAt
        );
    }

    @Override
    public Instant occurredOn() {
        return startedAt;
    }
}
