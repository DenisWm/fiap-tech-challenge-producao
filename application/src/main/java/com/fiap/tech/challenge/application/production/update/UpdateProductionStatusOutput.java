package com.fiap.tech.challenge.application.production.update;

import com.fiap.tech.challenge.domain.production.Production;

public record UpdateProductionStatusOutput(
        String id
) {

    public static UpdateProductionStatusOutput from(final Production aProduction) {
        return new UpdateProductionStatusOutput(aProduction.getId().getValue());
    }
}
