package com.fiap.tech.challenge.infrastructure.production.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiap.tech.challenge.application.production.retrieve.get.ProductionOutput;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductionResponse(
        @JsonProperty("id") String id,
        @JsonProperty("status") String status,
        @JsonProperty("received_at") String receivedAt,
        @JsonProperty("started_at") String startedAt,
        @JsonProperty("finished_at") String finishedAt,
        @JsonProperty("items") List<ItemResponse> items
) {

    public static ProductionResponse from(final ProductionOutput output) {
        return new ProductionResponse(
                output.id(),
                output.status().name(),
                output.receivedAt() != null ? output.receivedAt().toString() : null,
                output.startedAt() != null ? output.startedAt().toString() : null,
                output.finishedAt() != null ? output.finishedAt().toString() : null,
                extractItems(output)
        );
    }

    private static List<ItemResponse> extractItems(final ProductionOutput output) {
        return output.items().stream().map(ItemResponse::from).toList();
    }
}
