package com.fiap.tech.challenge.infrastructure.production.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiap.tech.challenge.application.production.retrieve.get.ItemOutput;
import com.fiap.tech.challenge.domain.production.Item;

public record ItemResponse(
        @JsonProperty("product_id") String productId,
        @JsonProperty("product_name") String productName,
        @JsonProperty("quantity") int quantity
) {

    public static ItemResponse from(final ItemOutput aItem) {
        return new ItemResponse(
                aItem.productId(),
                aItem.productName(),
                aItem.quantity()
        );
    }
}
