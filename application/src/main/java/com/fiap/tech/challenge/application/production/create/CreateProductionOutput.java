package com.fiap.tech.challenge.application.production.create;

import com.fiap.tech.challenge.domain.production.Production;

public record CreateProductionOutput(
        String id
) {

    public static CreateProductionOutput from(final Production aProduction) {
        return new CreateProductionOutput(aProduction.getId().getValue());
    }
}
