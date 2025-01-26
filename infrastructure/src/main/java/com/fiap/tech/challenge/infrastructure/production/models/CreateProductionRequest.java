package com.fiap.tech.challenge.infrastructure.production.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateProductionRequest(
        @JsonProperty("order_id") String orderId,
        @JsonProperty("status") String status,
        @JsonProperty("items") List<ItemRequest> items
) {
}
