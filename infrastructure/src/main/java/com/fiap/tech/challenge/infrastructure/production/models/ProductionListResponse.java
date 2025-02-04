package com.fiap.tech.challenge.infrastructure.production.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiap.tech.challenge.application.production.retrieve.list.ProductionListOutput;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductionListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("status") String status,
        @JsonProperty("received_at") String receivedAt,
        @JsonProperty("started_at") String startedAt
) {

    public static ProductionListResponse from(final ProductionListOutput output) {
        return new ProductionListResponse(
                output.id(),
                output.status().name(),
                output.receivedAt() != null ? output.receivedAt().toString() : null,
                output.startedAt() != null ? output.startedAt().toString() : null
        );
    }
}
