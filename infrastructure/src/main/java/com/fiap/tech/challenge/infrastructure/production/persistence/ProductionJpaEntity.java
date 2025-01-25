package com.fiap.tech.challenge.infrastructure.production.persistence;

import com.fiap.tech.challenge.domain.production.Item;
import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.domain.production.ProductionID;
import com.fiap.tech.challenge.domain.production.ProductionStatus;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Table(name = "productions")
@Entity(name = "Production")
public class ProductionJpaEntity {

    @Id
    private String id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductionStatus status;

    @Column(name = "received_at", columnDefinition = "TIMESTAMP")
    private Instant receivedAt;

    @Column(name = "started_at", columnDefinition = "TIMESTAMP")
    private Instant startedAt;

    @Column(name = "finished_at", columnDefinition = "TIMESTAMP")
    private Instant finishedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "productions_items", joinColumns = @JoinColumn(name = "production_id"))
    @Column(name = "item")
    private List<ItemJpaObject> items;

    public ProductionJpaEntity() {
    }

    private ProductionJpaEntity(
            final String id,
            final String orderId,
            final ProductionStatus status,
            final Instant receivedAt,
            final Instant startedAt,
            final Instant finishedAt,
            final List<ItemJpaObject> items
    ) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.receivedAt = receivedAt;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.items = items;
    }

    public static ProductionJpaEntity from(final Production aProduction) {
        return new ProductionJpaEntity(
                aProduction.getId().getValue(),
                aProduction.getOrderId(),
                aProduction.getStatus(),
                aProduction.getReceivedAt(),
                aProduction.getStartedAt(),
                aProduction.getFinishedAt(),
                extractItemsJpaObject(aProduction)
        );
    }

    public Production toAggregate() {
        return Production.with(
                ProductionID.from(this.id),
                this.orderId,
                this.status,
                this.receivedAt,
                this.startedAt,
                this.finishedAt,
                extractItemsValueObject()
        );
    }

    private List<Item> extractItemsValueObject() {
        return items.stream().map(ItemJpaObject::toValueObject).toList();
    }

    private static List<ItemJpaObject> extractItemsJpaObject(Production aProduction) {
        return aProduction.getItems().stream().map(ItemJpaObject::fromValueObject).toList();
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public ProductionJpaEntity setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public ProductionStatus getStatus() {
        return status;
    }

    public ProductionJpaEntity setStatus(ProductionStatus status) {
        this.status = status;
        return this;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public ProductionJpaEntity setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
        return this;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public ProductionJpaEntity setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public ProductionJpaEntity setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
        return this;
    }

    public List<ItemJpaObject> getItems() {
        return items;
    }

    public ProductionJpaEntity setItems(List<ItemJpaObject> items) {
        this.items = items;
        return this;
    }
}
