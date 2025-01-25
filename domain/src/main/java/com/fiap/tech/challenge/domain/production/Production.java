package com.fiap.tech.challenge.domain.production;

import com.fiap.tech.challenge.domain.AggregateRoot;
import com.fiap.tech.challenge.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Production extends AggregateRoot<ProductionID> {

    private String orderId;
    private ProductionStatus status;
    private Instant receivedAt;
    private Instant startedAt;
    private Instant finishedAt;
    private List<Item> items;


    protected Production(
            final ProductionID anId,
            final String orderId,
            final ProductionStatus status,
            final Instant receivedAt,
            final Instant startedAt,
            final Instant finishedAt,
            final List<Item> items
    ) {
        super(anId);
        this.orderId = orderId;
        this.status = status;
        this.receivedAt = receivedAt;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.items = new ArrayList<>(items == null ? Collections.emptyList() : items);

    }

    public static Production createProduction(final String orderId,
                                  final List<Item> items) {
        final var now = Instant.now();
        return new Production(ProductionID.unique(), orderId, ProductionStatus.RECEIVED, now, null, null, items);
    }

    public static Production with(final ProductionID anId,
                                  final String orderId,
                                  final ProductionStatus status,
                                  final Instant receivedAt,
                                  final Instant startedAt,
                                  final Instant finishedAt,
                                  final List<Item> items) {
        return new Production(
                anId,
                orderId,
                status,
                receivedAt,
                startedAt,
                finishedAt,
                items
        );
    }

    public Production updateStatus(final ProductionStatus newStatus) {
        this.status = newStatus;
        onStatusUpdate();
        return this;
    }

    private void onStatusUpdate() {
        if(status == null) {
            return;
        }
        if (this.status.equals(ProductionStatus.IN_PREPARATION)) {
            this.startedAt = Instant.now();
            this.registerEvent(ProductionInPreparation.with(getOrderId(), getStartedAt()));
        } else if (this.status.equals(ProductionStatus.READY)) {
            this.finishedAt = Instant.now();
            this.registerEvent(ProductionIsReady.with(getOrderId(), getFinishedAt()));
        }
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new ProductionValidator(this, handler).validate();
    }

    public String getOrderId() {
        return orderId;
    }

    public ProductionStatus getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public List<Item> getItems() {
        return items != null ? Collections.unmodifiableList(items) : Collections.emptyList();
    }

}
