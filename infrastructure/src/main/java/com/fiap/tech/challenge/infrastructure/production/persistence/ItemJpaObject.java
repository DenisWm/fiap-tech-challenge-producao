package com.fiap.tech.challenge.infrastructure.production.persistence;

import com.fiap.tech.challenge.domain.production.Item;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class ItemJpaObject {
    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    private ItemJpaObject(
            final String productId,
            final String productName,
            final int quantity
    ) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }

    public ItemJpaObject() {

    }

    public static ItemJpaObject fromValueObject(final Item aItem) {
        return new ItemJpaObject(
                aItem.productId(),
                aItem.productName(),
                aItem.quantity()
        );
    }

    public Item toValueObject() {
        return Item.of(this.productId, this.productName, this.quantity);
    }

    public String getProductId() {
        return productId;
    }

    public ItemJpaObject setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public ItemJpaObject setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ItemJpaObject setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
}
