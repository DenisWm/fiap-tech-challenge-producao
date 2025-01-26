package com.fiap.tech.challenge.infrastructure.production.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateProductionRequest(
        @JsonProperty("status") String status
) {
}
