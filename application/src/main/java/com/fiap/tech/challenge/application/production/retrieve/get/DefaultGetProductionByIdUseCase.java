package com.fiap.tech.challenge.application.production.retrieve.get;

import com.fiap.tech.challenge.domain.exceptions.NotFoundException;
import com.fiap.tech.challenge.domain.production.Production;
import com.fiap.tech.challenge.domain.production.ProductionGateway;
import com.fiap.tech.challenge.domain.production.ProductionID;

import java.util.Objects;

public class DefaultGetProductionByIdUseCase extends GetProductionByIdUseCase {

    private final ProductionGateway productionGateway;

    public DefaultGetProductionByIdUseCase(final ProductionGateway productionGateway) {
        this.productionGateway = Objects.requireNonNull(productionGateway);
    }

    @Override
    public ProductionOutput execute(final String anId) {
        final var productionId = ProductionID.from(anId);

        final var aProduction = productionGateway.findById(productionId)
                .orElseThrow(() -> NotFoundException.with(Production.class, productionId));

        return ProductionOutput.from(aProduction);
    }
}
