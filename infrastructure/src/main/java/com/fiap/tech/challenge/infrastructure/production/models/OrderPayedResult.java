package com.fiap.tech.challenge.infrastructure.production.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record OrderPayedResult(
        @JsonProperty("order_id") String orderID,
        @JsonProperty("items") List<ItemResult> items,
        @JsonProperty("occurred_on") Instant occurredOn
) {
}
