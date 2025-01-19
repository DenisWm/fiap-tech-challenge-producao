package com.fiap.tech.challenge.domain.production;

import com.fiap.tech.challenge.domain.events.DomainEvent;

import java.time.Instant;

public class ProductionInPreparation implements DomainEvent {

    String orderId;
    String status;
    Instant startedAt;


    private ProductionInPreparation(final String orderId, final String status, final Instant startedAt) {
        this.orderId = orderId;
        this.status = status;
        this.startedAt = startedAt;
    }

    public static ProductionInPreparation with(final String orderId, final Instant startedAt) {
        return new ProductionInPreparation(orderId, ProductionStatus.IN_PREPARATION.name().toLowerCase(), startedAt);
    }

    @Override
    public Instant occurredOn() {
        return startedAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }
}
