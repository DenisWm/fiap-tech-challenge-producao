package com.fiap.tech.challenge.domain.production;

import com.fiap.tech.challenge.domain.events.DomainEvent;

import java.time.Instant;

public class ProductionIsReady implements DomainEvent {

    String orderId;
    String status;
    Instant finishedAt;


    private ProductionIsReady(final String orderId, final String status, final Instant finishedAt) {
        this.orderId = orderId;
        this.status = status;
        this.finishedAt = finishedAt;
    }

    public static ProductionIsReady with(final String orderId, final Instant finishedAt) {
        return new ProductionIsReady(orderId, ProductionStatus.READY.name().toLowerCase(), finishedAt);
    }

    @Override
    public Instant occurredOn() {
        return finishedAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }
}
