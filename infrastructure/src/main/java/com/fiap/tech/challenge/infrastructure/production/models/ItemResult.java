package com.fiap.tech.challenge.infrastructure.production.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ItemResult(
        @JsonProperty("product_id") String productId,
        @JsonProperty("product_name") String productName,
        @JsonProperty("quantity") int quantity
) {
}
