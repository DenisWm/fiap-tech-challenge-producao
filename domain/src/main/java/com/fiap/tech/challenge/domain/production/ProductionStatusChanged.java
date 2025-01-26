package com.fiap.tech.challenge.domain.production;

import com.fiap.tech.challenge.domain.events.DomainEvent;

import java.time.Instant;

public class ProductionStatusChanged implements DomainEvent {

    String orderId;
    String status;
    Instant startedAt;
    Instant finishedAt;


    public ProductionStatusChanged(
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

    public String getOrderId() {
        return orderId;
    }

    public ProductionStatusChanged setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ProductionStatusChanged setStatus(String status) {
        this.status = status;
        return this;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public ProductionStatusChanged setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public ProductionStatusChanged setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
        return this;
    }
}
