package com.fiap.tech.challenge.domain.production;

import com.fiap.tech.challenge.domain.ValueObject;

import java.util.Objects;

public final class Item extends ValueObject {

    private final String productId;
    private final String productName;
    private final int quantity;

    private Item(final String productId, final String productName, final int quantity) {
        this.productId = Objects.requireNonNull(productId);
        this.productName = Objects.requireNonNull(productName);
        this.quantity = quantity;
    }

    public static Item of(final String productId, final String productName, final int quantity) {
        return new Item(productId, productName, quantity);
    }

    public String productId() {
        return productId;
    }

    public String productName() {
        return productName;
    }

    public int quantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final var items = (Item) o;
        return quantity == items.quantity && Objects.equals(productName, items.productName) && Objects.equals(productId, items.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, quantity);
    }
}
